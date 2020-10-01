package com.opentext.waterloo.coop.inspirationalquoteservice;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class Controller {
//    static int totalCounter = 0;
//    static int numberOfCalls = 0;
//    static Hashtable<String, Integer> ip_address = new Hashtable<String, Integer>();

    @Autowired
    private final QuoteRepository localQuoteRepository;
    @Autowired
    private final QuoteRepository remoteQuoteRepository;
    @Autowired
    private final NumbersOfCalls numbersOfCalls;

    public Controller(QuoteRepository localQuoteRepository, QuoteRepository remoteQuoteRepository, NumbersOfCalls numbersOfCalls) {
        this.localQuoteRepository = localQuoteRepository;
        this.remoteQuoteRepository = remoteQuoteRepository;
        this.numbersOfCalls = numbersOfCalls;
    }

    @SuppressWarnings("ConstantConditions")

    public JSONObject remoteFetchJSON() throws Exception {
        return remoteQuoteRepository.fetchJSON();
    }
    public JSONObject localFetchJSON() throws Exception {
        return localQuoteRepository.fetchJSON();
    }
    @GetMapping("/quote")
    public Quote quote() throws Exception {

        JSONObject json;
        //try to fetch online api quote
        try {
            json = remoteFetchJSON();
        } catch (Exception e) { //failed, try to fetch locally stored json
            json = localFetchJSON();
        }
        JSONObject quote = new JSONObject(json.getJSONObject("contents").getJSONArray("quotes").getString(0));

        String quoteOfTheDay = quote.get("quote").toString();
        String timestamp = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now());
        String author = quote.get("author").toString();
        String language = quote.get("language").toString();
        String image = quote.get("background").toString();
        String permalink = json.getJSONObject("copyright").get("url").toString();
        //missing numberOfCalls
        return new Quote(quoteOfTheDay, timestamp, numbersOfCalls.getNumberOfCalls(), author, language, image, permalink);
    }
}