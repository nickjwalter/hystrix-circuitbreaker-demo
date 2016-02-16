package hystrix.circuitbreaker.demo.controllers;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpStatus;
import org.junit.Test;

import com.jayway.restassured.response.Response;

import static com.jayway.restassured.RestAssured.get;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by nickwalter on 16/02/2016.
 */
public class BarcodeControllerRestTest {

    final String expectedBarcode = "iVBORw0KGgoAAAANSUhEUgAAAKcAAABJAQAAAABwIwuzAAAACXBIWXMAAA9hAAAPYQGoP6dpAAAAEnRFWHRT" +
        "b2Z0d2FyZQBCYXJjb2RlNEryjnYuAAAAmUlEQVR42mPQfVJqvnCbmM9Fq3znjNzlm77ci/HTYBgVHRUdFR0VHd6i/7GAf6SLMv7/X19fjy76Hihqh" +
        "yl6vl/ezl9eXn76fGTR7eX1sWbv3r1Pz0cWra6vj6369//ct+/Iosz99dnM8vJ639iRReu/12fXv3/37tt/NHPLgeaeRzX3fP/8cqAbzFHcAAffsfo4" +
        "H6vofIgoAOxlWOtNle0DAAAAAElFTkSuQmCC";

    private final String baseUrl = "http://localhost:9000/barcode?message=";

    @Test
    public void barcodeControllerRestTest() throws Exception {
        // when
        final Response response = get(baseUrl + "Test Message");

        // expect
        assertNotNull(response);
        assertEquals(HttpStatus.SC_OK, response.getStatusCode());

        assertNotNull(response.getBody());
        assertEquals(expectedBarcode, response.getBody().print());
    }

    @Test
    public void barcodeControllerVolumeRestTest() throws Exception {

        // given
        final List<String> messages = new ArrayList<>();
        for (int i=0; i < 10000; i++) {
            messages.add("Test Message " + i);
        }

        //when
        messages.parallelStream().forEach(
            message -> get(baseUrl + message)
        );
    }

}