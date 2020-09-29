package com.opentext.waterloo.coop.inspirationalquoteservice;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class Controller {

    private AtomicInteger numCalls;

    @GetMapping("/quote")
    public Quote quote() throws IOException, JSONException {

        //read local json file
        Resource resource = new ClassPathResource("plain.json");
        File file = resource.getFile();

        BufferedReader fileReader = new BufferedReader(new FileReader(file));
        StringBuilder builder = new StringBuilder();
        String r;
        while ((r=fileReader.readLine())!=null) {
            builder.append(r);
        }
        fileReader.close();

        String jsonStringFromFile = builder.toString();

        // If the file is empty, then use the method to query the quotes API
        // Note: this section hinges on the assumption that a fresh stringBuilder's toString method returns "".
        JSONObject json = jsonStringFromFile.equals("")?
                new JSONObject(jsonStringFromFile) : queryQuotesAPI();


        JSONObject quote = new JSONObject(json.getJSONObject("contents").getJSONArray("quotes").getString(0));

        String quoteOfTheDay = quote.get("quote").toString();
        String author = quote.get("author").toString();
        String timestamp = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now());
        String language = quote.get("language").toString();
        String image = quote.get("background").toString();
        String permalink = json.getJSONObject("copyright").get("url").toString();

        //missing numberOfCalls
        return new Quote(quoteOfTheDay, timestamp, numCalls.incrementAndGet(), author, language, image, permalink);

    }

    private JSONObject queryQuotesAPI() throws JSONException {

        //fetch live response
        String jsonString = null;
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
            jsonString = sb.toString();

            writeToLocalFile(jsonString);

        } catch (Exception e){
            System.out.println("Error occured " + e.getMessage());
        }


        return new JSONObject(jsonString);
    }

    private void writeToLocalFile(String jsonString) throws IOException{
        Resource resource = new ClassPathResource("plain.json");
        File file = resource.getFile();
        FileWriter writer = new FileWriter(file);

        writer.write(jsonString);
    }

    private void clearLocalFile() throws IOException{
        Resource resource = new ClassPathResource("plain.json");
        File file = resource.getFile();

        // Creating a writer with the "append" parameter set to false clears the file
        FileWriter writer = new FileWriter(file, false);
        writer.close();
    }
}