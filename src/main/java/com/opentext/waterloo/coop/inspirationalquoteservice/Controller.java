package com.opentext.waterloo.coop.inspirationalquoteservice;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class Controller {

    private static final Logger log = LoggerFactory.getLogger(InspirationalQuoteServiceApplication.class);

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
            log.info("Fetching live quote from api...");
            json = remoteFetchJSON();
        } catch (Exception e) { //failed, try to fetch locally stored json
            log.error("Live quote fetch failed! Returning local json file..." + e.getMessage());
            json = localFetchJSON();
        }
        JSONObject quote = new JSONObject(json.getJSONObject("contents").getJSONArray("quotes").getString(0));

        String quoteOfTheDay = quote.get("quote").toString();
        String timestamp = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now());
        String author = quote.get("author").toString();
        String language = quote.get("language").toString();
        String image = quote.get("background").toString();
        String permalink = json.getJSONObject("copyright").get("url").toString();

        Quote result = new Quote(quoteOfTheDay, timestamp, numbersOfCalls.getNumberOfCallsAndUpdate(), author, language, image, permalink);
        log.info("Quote has been created: " + "Client IP:" + numbersOfCalls.fetchClientIpAddr() + ", Number of Calls:" + numbersOfCalls.getNumberOfCalls() + ", Time Stamp:" + timestamp);
        return result;
    }
}