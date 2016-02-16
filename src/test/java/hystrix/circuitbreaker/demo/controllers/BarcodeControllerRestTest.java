package hystrix.circuitbreaker.demo.controllers;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpStatus;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Ignore;
import org.junit.Test;

import com.jayway.restassured.response.Response;

import static com.jayway.restassured.RestAssured.get;
import static hystrix.circuitbreaker.demo.services.BarcodeServiceTest.expectedBarcode;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Integration Test for the BarcodeController.
 * @Test
 */
@Ignore
public class BarcodeControllerRestTest {

    private final String baseUrl = "http://localhost:9000/barcode?message=";

    @Test
    public void barcodeControllerRestTest() throws Exception {
        // when
        final Response response = get(baseUrl + "Test Message");

        // expect
        assertNotNull(response);
        assertEquals(HttpStatus.SC_OK, response.getStatusCode());

        assertNotNull(response.getBody());

        JSONObject responseJSON = (JSONObject) new JSONParser().parse(response.getBody().print());
        assertEquals(expectedBarcode, responseJSON.get("encodedBarcodeImage"));
    }

    @Test
    public void barcodeControllerVolumeRestTest() throws Exception {

        // given
        final List<String> messages = new ArrayList<>();
        for (int i=0; i < 100; i++) {
            messages.add("Test Message " + i);
        }

        //when
        messages.parallelStream().forEach(
            message -> get(baseUrl + message)
        );
    }

}