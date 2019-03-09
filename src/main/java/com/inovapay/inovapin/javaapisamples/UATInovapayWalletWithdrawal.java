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
public class UATInovapayWalletWithdrawal {
    
    private static final Logger LOGGER = Logger.getLogger(UATInovapayWalletDeposit.class.getName());

    public static void main(String args[]) {
        try {
            LOGGER.log(Level.INFO, "API response {0}", process());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "error {0}", e);
        }

    }

    public static JSONObject process() throws Exception {
        JSONObject payload = new JSONObject();

        payload.put("reference", Math.random() * 10000); // Current transaction id, set by you !!!
        payload.put("user_id", "123456"); // User’s ID on Inovapay
        payload.put("user_login", "admin123"); // User’s Login on Merchant
        payload.put("user_name", "Admin"); // User’s Login on Merchant
        payload.put("user_secure_id", "123456789"); // User’s Secure ID on Inovapay
        payload.put("amount", "10"); // Transaction Value. Ex. (100.50)
        payload.put("currency", "brl"); // “usd” or “brl”
        payload.put("action", "create"); // “validate” or “create”

        InovapayRequest inovapayRequest = new InovapayRequest();
        String path = "/api/withdrawal";
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
