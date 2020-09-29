package com.opentext.waterloo.coop.inspirationalquoteservice;

import org.json.JSONObject;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class fetchAPI {

    @Cacheable("quotes")
    public JSONObject getAPI() throws Exception {
        String builder = null;

                //read local json file
     /*   Resource resource = new ClassPathResource("plain.json");
        File file = resource.getFile();

        BufferedReader fileReader = new BufferedReader(new FileReader(file));
        StringBuilder builder = new StringBuilder();
        String r;
        while ((r=fileReader.readLine())!=null) {
            builder.append(r);
        }
        fileReader.close();*/
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

        JSONObject json = new JSONObject(builder.toString());
        return json;
    }
}
