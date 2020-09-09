#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import java.time.Instant;

import org.json.JSONException;
import org.junit.Test;
import ${package}.model.Alert;
import org.skyscreamer.jsonassert.JSONAssert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AlertTest {

    private ObjectMapper mapper = new ObjectMapper();

    /**
     * Verifies that the object is serialized to JSON as expected.
     */
    @Test
    public void canSerializeToJson() throws JsonProcessingException, JSONException {
        Alert alert = new Alert();
        alert.setStatus(Alert.Status.CRITICAL);
        alert.setTimestamp(Instant.ofEpochSecond(1402302570));
        alert.setDescription("CPU is above upper limit (91%)");
        alert.setAttribute("my_unique_attribute", "my_unique_value");

        String expectedJson = "{\n" +
                "  \"status\": \"critical\",\n" +
                "  \"timestamp\": 1402302570,\n" +
                "  \"description\": \"CPU is above upper limit (91%)\",\n" +
                "  \"my_unique_attribute\": \"my_unique_value\"\n" +
                "}";
        JSONAssert.assertEquals(expectedJson, mapper.writeValueAsString(alert), false);
    }
}
