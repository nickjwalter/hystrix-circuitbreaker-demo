package hystrix.circuitbreaker.demo.services;

import org.json.simple.JSONObject;
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
     */
    public String barcodeMessage(final String msg) {
        final Code128Bean bean = new Code128Bean();
        final int dpi = 100;

        //Configure the barcode generator
        bean.setModuleWidth(UnitConv.in2mm(1.0f / dpi)); //makes the narrow bar
        bean.doQuietZone(false);

        final CommandGenerateBarcode generateBarcode = new CommandGenerateBarcode(bean, msg);
        final String base64EncodedBarcode = generateBarcode.execute();

        final JSONObject response = new JSONObject();
        response.put("inputMessage", msg);
        response.put("encodedBarcodeImage", base64EncodedBarcode);

        return response.toJSONString();
    }
}
