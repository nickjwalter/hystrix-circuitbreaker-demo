package hystrix.circuitbreaker.demo.services;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Unit Test For the Barcode Service.
 *
 * @author Nick Walter
 */
public class BarcodeServiceTest {

    BarcodeService barcodeService;

    @Before
    public void setUp() throws Exception {
        barcodeService = new BarcodeService();

        // clear the output file before starting..
        File outputFile = new File("barcode.png");
        if (outputFile.exists()) {
            outputFile.delete();
        }
    }

    @Test
    public void whenAMessageIsBarcodedSuccessfully() throws Exception {
        //given
        final String message = "Test Message";

        //when
        barcodeService.barcodeMessage(message);

        // expect
        File outputFile = new File("barcode.png");
        assertTrue(outputFile.exists());
    }
}