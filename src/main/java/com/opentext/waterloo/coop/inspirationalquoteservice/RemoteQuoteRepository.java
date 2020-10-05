package com.opentext.waterloo.coop.inspirationalquoteservice;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class RemoteQuoteRepository implements QuoteRepository{

    private static final Logger log = LoggerFactory.getLogger(InspirationalQuoteServiceApplication.class);

    @Override
    @Cacheable("quote")
    @Qualifier("remoteQuoteRepository")
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
            System.out.println("Error occurred " + e.getMessage());
        }

        return new JSONObject(builder);
    }

    //scheduled flush at midnight
    @Scheduled(cron = "0 0 0 * * ?")
    @CacheEvict(value = "quote", allEntries = true)
    public void clearCache() {
        log.info("Clearing cache.");
    }
}
