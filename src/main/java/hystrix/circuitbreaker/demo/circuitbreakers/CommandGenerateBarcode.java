package hystrix.circuitbreaker.demo.circuitbreakers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Base64;
import javax.imageio.ImageIO;

import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;

import com.netflix.config.DynamicPropertyFactory;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;

/**
 * Hystrix Circuit Breaker for Generating a Barcode
 *
 * @author Nick Walter
 */
public class CommandGenerateBarcode extends HystrixCommand<String> {

    private Code128Bean barcodeBean;
    private String message;

    public CommandGenerateBarcode(Code128Bean barcodeBean, String message) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("Barcode"))
            .andCommandKey(HystrixCommandKey.Factory.asKey("GenerateBarcodeImage")));

        this.barcodeBean = barcodeBean;
        this.message = message;
    }

    @Override
    protected String run() throws Exception {
        // force an error?
        if (DynamicPropertyFactory.getInstance().getBooleanProperty("demo.forceError", false).getValue()) {
            throw new RuntimeException("Ahhh I can't generate that barcode");
        }

        OutputStream out = new FileOutputStream(new File("barcode.png"));

        //Set up the canvas provider for monochrome PNG output
        BitmapCanvasProvider canvas = new BitmapCanvasProvider(
            out, "image/x-png", 100, BufferedImage.TYPE_BYTE_BINARY, false, 0);

        //Generate the barcode
        barcodeBean.generateBarcode(canvas, message);

        // introduce a delay
        Thread.sleep(
            DynamicPropertyFactory.getInstance().getIntProperty("demo.delay", 0).getValue()
        );

        //Signal end of generation and write to the file
        canvas.finish();
        out.close();

        //Output the Encoded Byte Array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(canvas.getBufferedImage(), "png", baos);
        baos.flush();
        byte[] imageInByte = baos.toByteArray();
        baos.close();

        return Base64.getEncoder().encodeToString(imageInByte);
    }

    @Override
    protected String getFallback() {
        return "Sorry, we couldn't generated your barcode.";
    }
}
