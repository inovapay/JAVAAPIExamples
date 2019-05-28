/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inovapay.inovapin.javaapisamples;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author dclav
 */
public class UATInovapayGatewayTranStatus {

    private static final Logger LOGGER = Logger.getLogger(UATInovapayGatewayTranStatus.class.getName());

    public static void main(String args[]) {
        try {
            LOGGER.log(Level.INFO, "API response {0}", process());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "error {0}", e);
        }

    }

    public static JSONObject process() throws Exception {
        JSONArray references = new JSONArray(); // Reference of past transaction given by Merchant, list is to be provided in case multiple queries
        JSONArray tranDates = new JSONArray(); // Transaction date. Can be single date to inquire a particular date, or two values to specify from and to date (range)

        JSONObject payload = new JSONObject();

        references.add("522125");
        references.add("522126");

        tranDates.add("2019-05-11"); // Dates on yyyy-MM-dd format.
        tranDates.add("2019-05-24"); // Dates on yyyy-MM-dd format.

        payload.put("status", "Canceled");// Transaction status to search (values: "Pending", "Completed", "Canceled")

        payload.put("reference", references);
        payload.put("date", tranDates);

        InovapayRequest inovapayRequest = new InovapayRequest();
        String path = "api/status";
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
