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
public class UATInovapayPinDateQuery {
    
    private static final Logger LOGGER = Logger.getLogger(UATInovapayPinMerTranQuery.class.getName());

    public static void main(String args[]) {
        try {
            LOGGER.log(Level.INFO, "API response {0}", process());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "error {0}", e);
        }

    }

    public static JSONObject process() throws Exception {
        JSONObject payload = new JSONObject();

        payload.put("fromDate", "2018-01-01"); // Initial date for search (yyyy-MM-dd)
        payload.put("toDate", "2019-04-04"); // Final date for search (yyyy-MM-dd)
        payload.put("terminalID", "Terminal1"); // Terminal id set by the merchant
        payload.put("reference", Math.random() * 10000); // Transaction number set by the merchant

        InovapayRequest inovapayRequest = new InovapayRequest();
        String path = "/inovapin/trans/between";
        String response = inovapayRequest.doRequest("POST", path, payload);
        return parseString(response);
    }

    private static JSONObject parseString(String data) throws Exception {
        LOGGER.log(Level.INFO, "Inovapay response raw data:{0}", data);
        JSONParser parser = new JSONParser();
        JSONObject result = (JSONObject) parser.parse(data);

        return result;
    }
    
}
