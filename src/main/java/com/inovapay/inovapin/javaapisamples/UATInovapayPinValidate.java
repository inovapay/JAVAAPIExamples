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
public class UATInovapayPinValidate {

    private static final Logger LOGGER = Logger.getLogger(UATInovapayPinValidate.class.getName());

    public static void main(String args[]) {
        try {
            LOGGER.log(Level.INFO, "API response {0}", process());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "error {0}", e);
        }

    }

    public static JSONObject process() throws Exception {
        JSONObject payload = new JSONObject();

        payload.put("pin", "1234567890123456"); // 16 digit user pin number
        payload.put("terminalID", "Terminal1"); // Terminal id set by the merchant
        payload.put("reference", Math.random() * 10000); // Reference number set by the merchant

        InovapayRequest inovapayRequest = new InovapayRequest();
        String path = "/inovapin/voucher/validate";
        String response = inovapayRequest.doRequest("POST", path, payload);
        JSONObject responseJSON = parseString(response);
        if(responseJSON.containsKey("jwt")){
            return inovapayRequest.decodeJWTResponseToken(responseJSON);
        }else{
            return responseJSON;
        }
        
    }

    private static JSONObject parseString(String data) throws Exception {
        LOGGER.log(Level.INFO, "Inovapay response raw data:{0}", data);
        JSONParser parser = new JSONParser();
        JSONObject result = (JSONObject) parser.parse(data);

        return result;
    }

}
