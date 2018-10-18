/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2018 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2018 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.integration.api.xml;

import java.util.List;
import java.util.stream.Collectors;

import org.opennms.integration.api.v1.config.events.AlarmData;
import org.opennms.integration.api.v1.config.events.AlarmType;
import org.opennms.integration.api.v1.config.events.EventDefinition;
import org.opennms.integration.api.v1.config.events.LogMessage;
import org.opennms.integration.api.v1.config.events.LogMsgDestType;
import org.opennms.integration.api.v1.config.events.ManagedObject;
import org.opennms.integration.api.v1.config.events.Mask;
import org.opennms.integration.api.v1.config.events.MaskElement;
import org.opennms.integration.api.v1.config.events.Parameter;
import org.opennms.integration.api.v1.config.events.UpdateField;
import org.opennms.integration.api.v1.config.events.Varbind;
import org.opennms.integration.api.v1.model.Severity;
import org.opennms.integration.api.xml.schema.eventconf.Event;
import org.opennms.integration.api.xml.schema.eventconf.Events;
import org.opennms.integration.api.xml.schema.eventconf.LogDestType;
import org.opennms.integration.api.xml.schema.eventconf.Logmsg;
import org.opennms.integration.api.xml.schema.eventconf.Maskelement;

/**
 * Used to load XML event definitions from the class-path.
 *
 * @author jwhite
 * @since 1.0.0
 */
public class ClasspathEventDefinitionLoader extends ClasspathXmlLoader<Events> {

    public ClasspathEventDefinitionLoader(Class<?> clazz, String... fileNames) {
        super(clazz, Events.class, "events", fileNames);
    }

    public List<EventDefinition> getEventDefinitions() {
        return getObjects().stream()
                .flatMap(e -> toEventDefinitions(e).stream())
                .collect(Collectors.toList());
    }

    private static List<EventDefinition> toEventDefinitions(Events events) {
        return events.getEvents().stream()
                .map(ClasspathEventDefinitionLoader::toEventDefinition)
                .collect(Collectors.toList());
    }

    private static EventDefinition toEventDefinition(Event e) {
        final Severity severity = Severity.get(e.getSeverity());
        final LogMessage logMessage = toLogMessage(e.getLogmsg());
        final AlarmData alarmData = toAlarmData(e.getAlarmData());
        final Mask mask = toMask(e.getMask());
        final List<Parameter> parameters = e.getParameters().stream()
                .map(ClasspathEventDefinitionLoader::toParameter)
                .collect(Collectors.toList());
        final EventDefinition def = new EventDefinition() {
            @Override
            public int getPriority() {
                return 0;
            }

            @Override
            public String getUei() {
                return e.getUei();
            }

            @Override
            public String getLabel() {
                return e.getEventLabel();
            }

            @Override
            public Severity getSeverity() {
                return severity;
            }

            @Override
            public String getDescription() {
                return e.getDescr();
            }

            @Override
            public LogMessage getLogMessage() {
                return logMessage;
            }

            @Override
            public AlarmData getAlarmData() {
                return alarmData;
            }

            @Override
            public Mask getMask() {
                return mask;
            }

            @Override
            public List<Parameter> getParameters() {
                return parameters;
            }
        };
        return def;
    }

    private static Parameter toParameter(org.opennms.integration.api.xml.schema.eventconf.Parameter p) {
        final Parameter parm = new Parameter() {
            @Override
            public String getName() {
                return p.getName();
            }

            @Override
            public String getValue() {
                return p.getValue();
            }

            @Override
            public boolean shouldExpand() {
                return p.getExpand();
            }
        };
        return parm;
    }

    private static Mask toMask(org.opennms.integration.api.xml.schema.eventconf.Mask eMask) {
        if (eMask == null) {
            return null;
        }
        final List<MaskElement> maskElements = eMask.getMaskelements().stream()
                .map(ClasspathEventDefinitionLoader::toMaskElemement)
                .collect(Collectors.toList());
        final List<Varbind> varbinds = eMask.getVarbinds().stream()
                .map(ClasspathEventDefinitionLoader::toVarbind)
                .collect(Collectors.toList());
        final Mask mask = new Mask() {
            @Override
            public List<MaskElement> getMaskElements() {
                return maskElements;
            }

            @Override
            public List<Varbind> getVarbinds() {
                return varbinds;
            }
        };
        return mask;
    }

    private static MaskElement toMaskElemement(Maskelement el) {
        final MaskElement maskElement = new MaskElement() {
            @Override
            public String getName() {
                return el.getMename();
            }

            @Override
            public List<String> getValues() {
                return el.getMevalues();
            }
        };
        return maskElement;
    }

    private static Varbind toVarbind(org.opennms.integration.api.xml.schema.eventconf.Varbind vb) {
        final Varbind varbind = new Varbind() {
            @Override
            public String getTextualConvention() {
                return vb.getTextualConvention();
            }

            @Override
            public Integer getNumber() {
                return vb.getVbnumber();
            }

            @Override
            public List<String> getValues() {
                return vb.getVbvalues();
            }
        };
        return varbind;
    }

    private static LogMessage toLogMessage(Logmsg log) {
        final LogMsgDestType dest = toLogMsgDestType(log.getDest());
        final LogMessage logMsg = new LogMessage() {
            @Override
            public String getContent() {
                return log.getContent();
            }

            @Override
            public LogMsgDestType getDestination() {
                return dest;
            }
        };
        return logMsg;
    }

    private static LogMsgDestType toLogMsgDestType(LogDestType dest) {
        if (dest != null) {
            switch(dest) {
                case LOGNDISPLAY:
                    return LogMsgDestType.LOGNDISPLAY;
                case DISPLAYONLY:
                    return LogMsgDestType.DISPLAYONLY;
                case LOGONLY:
                    return LogMsgDestType.LOGONLY;
                case SUPPRESS:
                    return LogMsgDestType.SUPPRESS;
                case DONOTPERSIST:
                    return LogMsgDestType.DONOTPERSIST;
                case DISCARDTRAPS:
                    return LogMsgDestType.DISCARDTRAPS;
            }
        }
        return LogMsgDestType.LOGNDISPLAY;
    }

    private static AlarmData toAlarmData(org.opennms.integration.api.xml.schema.eventconf.AlarmData alarm) {
        if (alarm == null) {
            return null;
        }
        final List<UpdateField> updateFields = alarm.getUpdateFields().stream()
                .map(ClasspathEventDefinitionLoader::toUpdateField)
                .collect(Collectors.toList());
        final AlarmType type = AlarmType.fromId(alarm.getAlarmType());
        final ManagedObject managedObject = toManagedObject(alarm.getManagedObject());
        final AlarmData alarmData = new AlarmData() {
            @Override
            public String getReductionKey() {
                return alarm.getReductionKey();
            }

            @Override
            public AlarmType getType() {
                return type;
            }

            @Override
            public String getClearKey() {
                return alarm.getClearKey();
            }

            @Override
            public boolean isAutoClean() {
                return alarm.getAutoClean();
            }

            @Override
            public List<UpdateField> getUpdateFields() {
                return updateFields;
            }

            @Override
            public ManagedObject getManagedObject() {
                return managedObject;
            }
        };
        return alarmData;
    }

    private static ManagedObject toManagedObject(org.opennms.integration.api.xml.schema.eventconf.ManagedObject mo) {
        if (mo == null) {
            return null;
        }
        final ManagedObject managedObject = new ManagedObject() {
            @Override
            public String getType() {
                return mo.getType();
            }
        };
        return managedObject;
    }

    private static UpdateField toUpdateField(org.opennms.integration.api.xml.schema.eventconf.UpdateField field) {
        final UpdateField updateField = new UpdateField() {
            @Override
            public String getName() {
                return field.getFieldName();
            }

            @Override
            public boolean isUpdatedOnReduction() {
                return field.getUpdateOnReduction();
            }
        };
        return updateField;
    }
}
