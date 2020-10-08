package com.opentext.waterloo.coop.inspirationalquoteservice;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

@Component
public class RemoteQuoteRepository implements QuoteRepository{

    @Autowired
    Environment env;

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
            urlc.setConnectTimeout(3000);

            BufferedReader br = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line=br.readLine())!=null) {
                sb.append(line).append('\n');
            }
            br.close();
            builder = sb.toString();
        } catch (java.net.SocketTimeoutException e) {
            log.error("Connection timeout! " + e.getMessage());
        } catch (java.io.IOException e) {
            log.error("IOException occurred! " + e.getMessage());
        }
        // only enable file overwriting file in active dev environment
        if(Arrays.stream(env.getActiveProfiles()).anyMatch(
                env -> (env.equalsIgnoreCase("dev"))) && builder != null) {
            writeUsingBufferedWriter(builder);
        }
        return new JSONObject(builder);
    }

    private void writeUsingBufferedWriter(String data) {
        File file = null;
        try {
            file = new File("src/main/resources/plain.json");
            BufferedWriter br = new BufferedWriter(new FileWriter(file));
            br.write(data);
            br.flush();
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //scheduled flush at midnight
    @Scheduled(cron = "0 0 0 * * ?")
    @CacheEvict(value = "quote", allEntries = true)
    public void clearCache() {
        log.info("Clearing cache.");
    }
}
