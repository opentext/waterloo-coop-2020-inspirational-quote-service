package com.opentext.waterloo.coop.inspirationalquoteservice;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class Controller {

    @Autowired
    private final QuoteRepository localQuoteRepository;
    @Autowired
    private final QuoteRepository remoteQuoteRepository;

    public Controller(QuoteRepository localQuoteRepository, QuoteRepository remoteQuoteRepository) {
        this.localQuoteRepository = localQuoteRepository;
        this.remoteQuoteRepository = remoteQuoteRepository;
    }

    @GetMapping("/quote")
    public Quote quote() throws Exception {

        JSONObject json;
        //try to fetch online api quote
        try {
            json = remoteQuoteRepository.fetchJSON();
        } catch (Exception e) { //failed, try to fetch locally stored json
            json = localQuoteRepository.fetchJSON();
        }
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