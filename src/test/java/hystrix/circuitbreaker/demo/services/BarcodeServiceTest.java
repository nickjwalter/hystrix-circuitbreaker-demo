package hystrix.circuitbreaker.demo.services;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit Test For the Barcode Service.
 *
 * @author Nick Walter
 */
public class BarcodeServiceTest {

    BarcodeService barcodeService;

    final String expectedBarcode = "iVBORw0KGgoAAAANSUhEUgAAAKcAAABJAQAAAABwIwuzAAAAmUlEQVR42mPQfVJqvnCbmM9Fq3znjNzlm77" +
        "ci/HTYBgVHRUdFR0VHd6i/7GAf6SLMv7/X19fjy76Hihqhyl6vl/ezl9eXn76fGTR7eX1sWbv3r1Pz0cWra6vj6369//ct+/Iosz99dnM8vJ63" +
        "9iRReu/12fXv3/37tt/NHPLgeaeRzX3fP/8cqAbzFHcAAffsfo4H6vofIgoAOxlWOtNle0DAAAAAElFTkSuQmCC";

    @Before
    public void setUp() throws Exception {
        barcodeService = new BarcodeService();
    }

    @Test
    public void whenAMessageIsBarcodedSuccessfully() throws Exception {
        //given
        final String message = "Test Message";

        //when
        final String response = barcodeService.barcodeMessage(message);

        // expect
        assertNotNull(response);

        JSONObject responseJSON = (JSONObject) new JSONParser().parse(response);

        assertEquals(expectedBarcode, responseJSON.get("encodedBarcodeImage"));
        assertEquals(message, responseJSON.get("inputMessage"));
    }
}