package com.opentext.waterloo.coop.inspirationalquoteservice;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class RemoteQuoteRepository implements QuoteRepository{
    @Override
    public JSONObject fetchJSON() throws Exception {

        //fetch live response
        String builder = null;
        try {
            URL url = new URL("https://quotes.rest/qod?category=inspire");
            HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
            urlc.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line=br.readLine())!=null) {
                sb.append(line).append('\n');
            }
            br.close();
            builder = sb.toString();
        } catch (Exception e){
            System.out.println("Error occured " + e.getMessage());
        }

        return new JSONObject(builder);
    }
}