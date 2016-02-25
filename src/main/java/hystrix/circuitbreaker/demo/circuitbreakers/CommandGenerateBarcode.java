package hystrix.circuitbreaker.demo.circuitbreakers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
 * Circuit Breaker to Wrap Generating a Barcode Image.
 *
 * @author nickwalter
 */
public class CommandGenerateBarcode extends HystrixCommand<String> {

    Code128Bean barcodeBean;
    String msg;

    public CommandGenerateBarcode(Code128Bean bean, String message) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("BarcodeService"))
            .andCommandKey(HystrixCommandKey.Factory.asKey("GenerateBarcode")));

        this.barcodeBean = bean;
        this.msg = message;
    }

    @Override
    protected String run() throws Exception {

        Thread.sleep(
            DynamicPropertyFactory.getInstance().getIntProperty("demo.delay", 1).getValue()
        );

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

            return Base64.getEncoder().encodeToString(imageInByte);

        } catch (IOException e) {
            throw e;
        }
    }

    @Override
    public String getFallback() {
        return "Sorry, we couldn't build your barcode";
    }

}
