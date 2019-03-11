/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inovapay.inovapin.javaapisamples;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author dclav
 */
public class UATInovapayGatewayTransparent {
    
    private static final Logger LOGGER = Logger.getLogger(UATInovapayGatewayTransparent.class.getName());

    public static void main(String args[]) {
        try {
            LOGGER.log(Level.INFO, "API response {0}", process());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "error {0}", e);
        }

    }

    public static JSONObject process() throws Exception {
        JSONObject payload = new JSONObject();

        payload.put("name", "John Doe"); // Initial date for search (yyyy-MM-dd)
        payload.put("email", "john.doe@hotmail.com"); // Final date for search (yyyy-MM-dd)
        payload.put("cpf", "56936238157"); // Terminal id set by you !!!
        payload.put("paymentMethod", "caixa"); // Transaction number set by you !!!
        payload.put("amount", "10"); // Initial date for search (yyyy-MM-dd)
        payload.put("currency", "brl"); // Final date for search (yyyy-MM-dd)
        payload.put("reference", Math.random() * 10000); // Terminal id set by you !!!
        payload.put("merchant_user", "johnDoe"); // Transaction number set by you !!!
        
        InovapayRequest inovapayRequest = new InovapayRequest();
        String path = "/direct/deposit";
        String response = inovapayRequest.doDirectRequest("GET", path, payload);
        return parseString(response);
    }

    private static JSONObject parseString(String data) throws Exception {
        LOGGER.log(Level.INFO, "Inovapay response raw data:{0}", data);
        JSONParser parser = new JSONParser();
        JSONObject result = (JSONObject) parser.parse(data);

        return result;
    }
    
}
