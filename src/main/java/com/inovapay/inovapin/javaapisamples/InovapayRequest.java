/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inovapay.inovapin.javaapisamples;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

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
    private final String MERCHANT_API_KEY = "API_KEY"; // Your API key
    private final String MERCHANT_API_SECRET = "API_SECRET"; // Your API secret

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
                .setExpiration(Date.from(Instant.ofEpochMilli((new Date()).getTime() + 60000)))
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

        HttpURLConnection httpConn = (HttpURLConnection) conn;
        InputStream _is;
        if (httpConn.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
            _is = httpConn.getInputStream();
        } else {
            /* error from server */
            _is = httpConn.getErrorStream();
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(
                (_is)));

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
     * Method to do the final call to inovapay gateway services through HTTP
     * request.
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

        URL url = new URL(siteUrl + "/" + MERCHANT_API_KEY + "/" + jwtData);
        LOGGER.log(Level.INFO, "URL: {0}", url);
        return "";
    }

    public JSONObject decodeJWTResponseToken(JSONObject jwtCoded) throws Exception{
        LOGGER.log(Level.INFO, "jwt:{0}***", jwtCoded);
        LOGGER.log(Level.INFO, "apiSecret:{0}***", MERCHANT_API_SECRET);

        Claims claims = Jwts.parser()
                .setSigningKey(MERCHANT_API_SECRET.getBytes())
                .parseClaimsJws(jwtCoded.get("jwt").toString()).getBody();

        LOGGER.log(Level.INFO, "ID: {0}", claims.getId());
        LOGGER.log(Level.INFO, "Subject: {0}", claims.getSubject());
        LOGGER.log(Level.INFO, "Issuer: {0}", claims.getIssuer());
        LOGGER.log(Level.INFO, "Expiration: {0}", claims.getExpiration());
        LOGGER.log(Level.INFO, "Claim: {0}", claims);
        LOGGER.log(Level.INFO, "Claim Keys: {0}", claims.keySet());

        JSONObject result = new JSONObject(claims);

        JSONParser parser = new JSONParser();

        return (JSONObject) ((JSONObject) parser.parse(result.toJSONString()));
    }

}
