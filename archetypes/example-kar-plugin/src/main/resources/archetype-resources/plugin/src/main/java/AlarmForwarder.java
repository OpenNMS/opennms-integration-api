#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import java.util.List;
import java.util.Objects;

import org.opennms.integration.api.v1.alarms.AlarmLifecycleListener;
import org.opennms.integration.api.v1.events.EventForwarder;
import org.opennms.integration.api.v1.model.Alarm;
import org.opennms.integration.api.v1.model.immutables.ImmutableEventParameter;
import org.opennms.integration.api.v1.model.immutables.ImmutableInMemoryEvent;
import ${package}.model.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;

public class AlarmForwarder implements AlarmLifecycleListener {
    private static final Logger LOG = LoggerFactory.getLogger(AlarmForwarder.class);

    private static final String UEI_PREFIX = "uei.opennms.org/${pluginId}Plugin";
    private static final String SEND_EVENT_FAILED_UEI = UEI_PREFIX + "/sendEventFailed";
    private static final String SEND_EVENT_SUCCESSFUL_UEI = UEI_PREFIX + "/sendEventSuccessful";

    private final MetricRegistry metrics = new MetricRegistry();
    private final Meter eventsForwarded = metrics.meter("eventsForwarded");
    private final Meter eventsFailed = metrics.meter("eventsFailed");

    private final ApiClient apiClient;
    private final EventForwarder eventForwarder;

    public AlarmForwarder(ApiClient apiClient, EventForwarder eventForwarder) {
        this.apiClient = Objects.requireNonNull(apiClient);
        this.eventForwarder = Objects.requireNonNull(eventForwarder);
    }

    @Override
    public void handleNewOrUpdatedAlarm(Alarm alarm) {
        if (alarm.getReductionKey().startsWith(UEI_PREFIX)) {
            // Never forward alarms that the plugin itself creates
            return;
        }

        // Map the alarm to the corresponding model object that the API requires
        Alert alert = toAlert(alarm);

        // Forward the alarm
        apiClient.sendAlert(alert).whenComplete((v,ex) -> {
            if (ex != null) {
                eventsForwarded.mark();
                eventForwarder.sendAsync(ImmutableInMemoryEvent.newBuilder()
                        .setUei(SEND_EVENT_FAILED_UEI)
                        .addParameter(ImmutableEventParameter.newBuilder()
                                .setName("reductionKey")
                                .setValue(alarm.getReductionKey())
                                .build())
                        .addParameter(ImmutableEventParameter.newBuilder()
                                .setName("message")
                                .setValue(ex.getMessage())
                                .build())
                        .build());
                LOG.warn("Sending event for alarm with reduction-key: {} failed.", alarm.getReductionKey(), ex);
            } else {
                eventsFailed.mark();
                eventForwarder.sendAsync(ImmutableInMemoryEvent.newBuilder()
                        .setUei(SEND_EVENT_SUCCESSFUL_UEI)
                        .addParameter(ImmutableEventParameter.newBuilder()
                                .setName("reductionKey")
                                .setValue(alarm.getReductionKey())
                                .build())
                        .build());
                LOG.info("Event sent successfully for alarm with reduction-key: {}", alarm.getReductionKey());
            }
        });
    }

    @Override
    public void handleAlarmSnapshot(List<Alarm> alarms) {
        // pass
    }

    @Override
    public void handleDeletedAlarm(int alarmId, String reductionKey) {
        // pass
    }

    public static Alert toAlert(Alarm alarm) {
        Alert alert = new Alert();
        alert.setStatus(toStatus(alarm));
        alert.setDescription(alarm.getDescription());
        return alert;
    }

    private static Alert.Status toStatus(Alarm alarm) {
        if (alarm.isAcknowledged()) {
            return Alert.Status.ACKNOWLEDGED;
        }
        switch (alarm.getSeverity()) {
            case INDETERMINATE:
            case CLEARED:
            case NORMAL:
                return Alert.Status.OK;
            case WARNING:
            case MINOR:
                return Alert.Status.WARNING;
            case MAJOR:
            case CRITICAL:
            default:
                return Alert.Status.CRITICAL;
        }
    }

    public MetricRegistry getMetrics() {
        return metrics;
    }
}
