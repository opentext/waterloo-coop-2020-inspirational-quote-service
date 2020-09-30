package com.opentext.waterloo.coop.inspirationalquoteservice;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

@Component
public class LocalQuoteRepository implements QuoteRepository{
    @Override
    @Qualifier("localQuoteRepository")
    public JSONObject fetchJSON() throws Exception {

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
        return new JSONObject(builder.toString());
    }
}
