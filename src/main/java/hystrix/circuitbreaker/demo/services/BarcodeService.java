package hystrix.circuitbreaker.demo.services;

import java.io.IOException;

import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.tools.UnitConv;
import org.springframework.stereotype.Component;

import hystrix.circuitbreaker.demo.circuitbreakers.CommandGenerateBarcode;

/**
 * Service to generate barcodes
 *
 * @author Nick Walter
 */
@Component("barcodeService")
public class BarcodeService {

    /**
     * Encode a Message in a barcode.
     * @param msg the message to encode
     * @throws IOException
     */
    public String barcodeMessage(final String msg) {
        final Code128Bean barcodeBean = new Code128Bean();
        final int dpi = 100;

        //Configure the barcode generator
        barcodeBean.setModuleWidth(UnitConv.in2mm(1.0f / dpi)); //makes the narrow bar
        barcodeBean.doQuietZone(false);

        final CommandGenerateBarcode circuitBreaker = new CommandGenerateBarcode(
            barcodeBean,
            msg
        );

        return circuitBreaker.execute();
    }
}
