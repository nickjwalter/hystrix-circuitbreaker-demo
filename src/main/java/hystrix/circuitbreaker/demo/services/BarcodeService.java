package hystrix.circuitbreaker.demo.services;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;
import javax.imageio.ImageIO;

import org.json.simple.JSONObject;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;
import org.springframework.stereotype.Component;

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

        try (OutputStream out = new FileOutputStream(new File("barcode.png"))) {


            //Set up the canvas provider for monochrome PNG output
            BitmapCanvasProvider canvas = new BitmapCanvasProvider(
                out, "image/x-png", 100, BufferedImage.TYPE_BYTE_BINARY, false, 0);

            //Generate the barcode
            barcodeBean.generateBarcode(canvas, msg);

            //Signal end of generation and write to the file
            canvas.finish();
            out.close();

            //Output the Encoded Byte Array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(canvas.getBufferedImage(), "png", baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            baos.close();

            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("inputMessage", msg);
            jsonObject.put("encodedBarcodeImage", Base64.getEncoder().encodeToString(imageInByte));

            return jsonObject.toJSONString();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
