package com.opentext.waterloo.coop.inspirationalquoteservice;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class Controller {

    @GetMapping("/quote")
    public Quote quote(@RequestParam(value = "name", defaultValue = "World") String name) throws Exception {
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

//        //read local json file
//        Resource resource = new ClassPathResource("plain.json");
//        File file = resource.getFile();
//
//        BufferedReader fileReader = new BufferedReader(new FileReader(file));
//        StringBuilder builder = new StringBuilder();
//        String r;
//        while ((r=fileReader.readLine())!=null) {
//            builder.append(r);
//        }
//        fileReader.close();

        JSONObject json = new JSONObject(builder.toString());
        JSONObject quote = new JSONObject(json.getJSONObject("contents").getJSONArray("quotes").getString(0));

        String quoteOfTheDay = quote.get("quote").toString();
        String timestamp = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now());
        String author = quote.get("author").toString();
        String language = quote.get("language").toString();
        String image = quote.get("background").toString();
        String permalink = json.getJSONObject("copyright").get("url").toString();

        //missing numberOfCalls
        return new Quote(quoteOfTheDay, timestamp, -1, author, language, image, permalink);
//        Quote(String quoteOfTheDay, String timestamp, int numberOfCalls, String author, String language, String image, String permalink)
    }
}