package hystrix.circuitbreaker.demo.services;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

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
    public void barcodeMessage(final String msg) throws IOException {
        final Code128Bean bean = new Code128Bean();
        final int dpi = 100;

        //Configure the barcode generator
        bean.setModuleWidth(UnitConv.in2mm(1.0f / dpi)); //makes the narrow bar
        bean.doQuietZone(false);

        //Open output file
        File outputFile = new File("barcode.png");
        OutputStream out = new FileOutputStream(outputFile);

        try {
            //Set up the canvas provider for monochrome PNG output
            BitmapCanvasProvider canvas = new BitmapCanvasProvider(
                out, "image/x-png", dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0);

            //Generate the barcode
            bean.generateBarcode(canvas, msg);

            //Signal end of generation
            canvas.finish();

        } finally {
            out.close();
        }
    }
}
