/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inovapay.inovapin.javaapisamples;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONObject;

/**
 * Class user to make the call to inovapay redeem services. 
 * 
 * @author dclav
 */
public class InovapayRequest {

    private static final Logger LOGGER = Logger.getLogger(InovapayRequest.class.getName());

    protected static final char[] HEX_ARRAY = "0123456789abcdef".toCharArray();

    private final String ROOT_PATH = "https://uat.inovapay.com/"; // UAT inovapay base url
    private String siteUrl;
    private final String MERCHANT_API_KEY = "9396735"; // Your API key
    private final String MERCHANT_API_SECRET = "ee0123a639e3fecc6fb7b83a4318186b6950b172"; // Your API secret

    /**
     * Constructor
     * 
     */
    public InovapayRequest() {
           
    }

    /**
     * Method to do the final call to inovapay services through HTTP request.
     * 
     * @param requestMethod Request method (POST, PUT, GET, etc)
     * @param requestUri Request uri of the service
     * @param body Request body to be sent to inovapay service. 
     * @return String representation of inovapay response. 
     * @throws Exception
     */
    public String doRequest(String requestMethod, String requestUri, JSONObject body) throws Exception {

        siteUrl = ROOT_PATH + requestUri;

        LOGGER.log(Level.FINE, "Payload raw:{0}", body);

        String jwtData = Jwts.builder()
                .setIssuer("INOVAPIN")
                .setSubject("REDEEM")
                .claim("data", body)
                .setIssuedAt(Date.from(Instant.ofEpochMilli((new Date()).getTime() - 500)))
                .setExpiration(Date.from(Instant.ofEpochMilli((new Date()).getTime() + 6000)))
                .signWith(
                        SignatureAlgorithm.HS256, MERCHANT_API_SECRET.getBytes("UTF-8")
                )
                .compact();

        JSONObject jwt = new JSONObject();

        jwt.put("jwt", jwtData);

        LOGGER.log(Level.INFO, "URL:", siteUrl);

        URL url = new URL(siteUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setDoOutput(true);
        conn.setInstanceFollowRedirects(false);
        conn.setRequestMethod(requestMethod);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("x-api-key", MERCHANT_API_KEY);
        conn.setUseCaches(false);

        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(conn.getOutputStream());
        outputStreamWriter.write(jwt.toJSONString());
        outputStreamWriter.flush();

        conn.connect();

        if (conn.getResponseCode() != 200) {
            LOGGER.log(Level.SEVERE, "HTTP Code:", conn.getResponseCode());
            
            LOGGER.log(Level.SEVERE, "----------------------------------");
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(
                (conn.getInputStream())));

        String output;
        StringBuilder response = new StringBuilder();
        while ((output = br.readLine()) != null) {
            response.append(output);
        }

        LOGGER.log(Level.INFO, "Response:", response.toString());

        conn.disconnect();

        return response.toString();
    }
    
    /**
     * Method to do the final call to inovapay gateway services through HTTP request.
     * 
     * @param requestMethod Request method (POST, PUT, GET, etc)
     * @param requestUri Request uri of the service
     * @param body Request body to be sent to inovapay service. 
     * @return String representation of inovapay response. 
     * @throws Exception
     */
    public String doDirectRequest(String requestMethod, String requestUri, JSONObject body) throws Exception {

        siteUrl = ROOT_PATH + requestUri;

        LOGGER.log(Level.FINE, "Payload raw:{0}", body);

        String jwtData = Jwts.builder()
                .setIssuer("INOVAPIN")
                .setSubject("REDEEM")
                .claim("data", body)
                .setIssuedAt(Date.from(Instant.ofEpochMilli((new Date()).getTime() - 500)))
                .setExpiration(Date.from(Instant.ofEpochMilli((new Date()).getTime() + 15000)))
                .signWith(
                        SignatureAlgorithm.HS256, MERCHANT_API_SECRET.getBytes("UTF-8")
                )
                .compact();

        LOGGER.log(Level.INFO, "URL: {0}", siteUrl);

    
        URL url = new URL(siteUrl + "/" + MERCHANT_API_KEY + "/" +jwtData );
        LOGGER.log(Level.INFO, "URL: {0}", url);
        return "";
    }

}
