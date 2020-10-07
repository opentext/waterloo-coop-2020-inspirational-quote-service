package com.opentext.waterloo.coop.inspirationalquoteservice;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
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
            urlc.setConnectTimeout(2500);

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
        if (builder != null){
            writeUsingFileWriter(builder);
        }
        return new JSONObject(builder);
    }

    private void writeUsingFileWriter(String data) throws FileNotFoundException {
        PrintWriter writer =
                new PrintWriter(
                        new File(this.getClass().getResource("plain.json").getPath()));
        writer.write(data);
        writer.flush();
        writer.close();
    }

    //scheduled flush at midnight
    @Scheduled(cron = "0 0 0 * * ?")
    @CacheEvict(value = "quote", allEntries = true)
    public void clearCache() {
        log.info("Clearing cache.");
    }
}
