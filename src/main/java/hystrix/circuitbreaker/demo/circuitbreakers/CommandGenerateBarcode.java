package hystrix.circuitbreaker.demo.circuitbreakers;

import org.krysalis.barcode4j.impl.code128.Code128Bean;

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
        return null;
    }

}
