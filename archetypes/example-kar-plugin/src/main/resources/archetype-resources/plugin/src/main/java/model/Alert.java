#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.model;

import java.io.IOException;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Alert {

    @JsonProperty("status")
    @JsonSerialize(using = Status.Serializer.class)
    private Status status;

    @JsonProperty("timestamp")
    @JsonSerialize(using = InstantSerializer.class)
    private Instant timestamp;

    @JsonProperty("description")
    private String description;

    private Map<String,String> attributes = new LinkedHashMap<>();

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonAnyGetter
    public Map<String, String> getAttributes() {
        return attributes;
    }

    @JsonAnySetter
    public void setAttribute(String name, String value) {
        attributes.put(name, value);
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Alert alert = (Alert) o;
        return Objects.equals(status, alert.status) &&
                Objects.equals(timestamp, alert.timestamp) &&
                Objects.equals(description, alert.description) &&
                Objects.equals(attributes, alert.attributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, timestamp, description, attributes);
    }

    @Override
    public String toString() {
        return "Alert{" +
                "status='" + status + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", description='" + description + '\'' +
                ", attributes=" + attributes +
                '}';
    }

    public enum Status {
        OK,
        CRITICAL,
        WARNING,
        ACKNOWLEDGED;

        public static class Serializer extends StdSerializer<Status> {
            protected Serializer() {
                super(Status.class);
            }

            @Override
            public void serialize(Status status, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeString(status.name().toLowerCase());
            }
        }
    }

    public static class InstantSerializer extends StdSerializer<Instant> {
        protected InstantSerializer() {
            super(Instant.class);
        }

        @Override
        public void serialize(Instant instant, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeNumber(instant.getEpochSecond());
        }
    }
}
