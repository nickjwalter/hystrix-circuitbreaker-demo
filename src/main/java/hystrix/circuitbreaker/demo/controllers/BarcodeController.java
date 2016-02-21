package hystrix.circuitbreaker.demo.controllers;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hystrix.circuitbreaker.demo.services.BarcodeService;

/**
 * Rest Endpoint for Generating a 1D Barcode of a message
 *
 * @author Nick Walter
 */
@RestController
public class BarcodeController {

    @Autowired
    BarcodeService barcodeService;

    @RequestMapping("/barcode")
    public String barcode(@RequestParam(value="message", defaultValue="Nicks Barcode") String message) {

        final String barcodeImage = barcodeService.barcodeMessage(message);

        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("inputMessage", message);
        jsonObject.put("encodedBarcodeImage", barcodeImage);

        return jsonObject.toJSONString();
    }

}

