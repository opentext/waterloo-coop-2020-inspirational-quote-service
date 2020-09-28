package com.opentext.waterloo.coop.inspirationalquoteservice;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.atomic.AtomicLong;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();


    @GetMapping("/quote")
    public Quote quote(@RequestParam(value = "name", defaultValue = "World") String name) throws Exception {
        URL url = new URL("https://quotes.rest/qod?category=funny");
        StringBuilder buffer = new StringBuilder();
        try {
            HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
            urlc.setRequestMethod("GET");
            // set the content type
            urlc.setRequestProperty("Content-Type", "application/json");
            urlc.setRequestProperty("X-TheySaidSo-Api-Secret", "YOUR API KEY HERE");
            //System.out.println("Connect to: " + url.toString());
            urlc.setAllowUserInteraction(false);
            urlc.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
            String l = null;

            //while ((l=br.readLine())!=null) {
            //    buffer.append(l);
            //}
            br.close();
        } catch (Exception e){
            System.out.println("Error occured ???");
            System.out.println(e.toString());
        }

        File file = new File("/Users/jindongche/Desktop/Git/src/main/java/com/opentext/waterloo/coop/inspirationalquoteservice/plain.json");
        BufferedReader fileReader = new BufferedReader(new FileReader(file));
        String r;
        while ((r=fileReader.readLine())!=null) {
            buffer.append(r);
        }

        JSONObject json = new JSONObject(buffer.toString());
        JSONObject quote = new JSONObject(json.getJSONObject("contents").getJSONArray("quotes").getString(0));

        return new Quote(-1, quote.getString("quote"));
    }
}