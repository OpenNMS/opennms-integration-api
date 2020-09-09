#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import org.json.JSONException;
import org.junit.Test;
import ${package}.model.Topology;
import org.skyscreamer.jsonassert.JSONAssert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TopologyTest {

    private ObjectMapper mapper = new ObjectMapper();

    /**
     * Verifies that the object is serialized to JSON as expected.
     */
    @Test
    public void canSerializeToJson() throws JsonProcessingException, JSONException {
        Topology topology = new Topology();
        topology.addLink("host", "check");
        topology.addLink("host", "cluster");

        String expectedJson = "{ \n" +
                "      \"links\":[ \n" +
                "         { \n" +
                "            \"source\":\"host\",\n" +
                "            \"target\":\"check\"\n" +
                "         },\n" +
                "         { \n" +
                "            \"source\":\"host\",\n" +
                "            \"target\":\"cluster\"\n" +
                "         }\n" +
                "      ]\n" +
                "}";
        JSONAssert.assertEquals(expectedJson, mapper.writeValueAsString(topology), false);
    }
}
