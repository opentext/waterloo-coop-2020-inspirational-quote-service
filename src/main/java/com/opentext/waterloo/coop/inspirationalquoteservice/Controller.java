package com.opentext.waterloo.coop.inspirationalquoteservice;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@RestController
public class Controller {
    static int numberOfCalls = 0;
    @Autowired
    private final LocalQuoteRepository localQuoteRepository;
    @Autowired
    private final QuoteRepository remoteQuoteRepository;

    public Controller(LocalQuoteRepository localQuoteRepository, QuoteRepository remoteQuoteRepository) {
        this.localQuoteRepository = localQuoteRepository;
        this.remoteQuoteRepository = remoteQuoteRepository;
    }

    @SuppressWarnings("ConstantConditions")
    protected String fetchClientIpAddr(){
        HttpServletRequest request = ((ServletRequestAttributes)(RequestContextHolder.getRequestAttributes())).getRequest();
        String ip = Optional.ofNullable(request.getHeader("X-FORWARDED-FOR")).orElse(request.getRemoteAddr());
        if (ip.equals("0:0:0:0:0:0:1")) ip = "127.0.0.1";
//        Assert.isTrue(ip.chars().filter($ -> $ == '.').count() == 3,"Illegal IP:" + ip);
        return ip;
    }

    @GetMapping("/quote")
    public Quote quote() throws Exception {

        JSONObject json;
        String ip = fetchClientIpAddr();
        //try to fetch online api quote
        try {
            json = localQuoteRepository.fetchJSON();
        } catch (Exception e) { //failed, try to fetch locally stored json
            json = remoteQuoteRepository.fetchJSON();
        }
        JSONObject quote = new JSONObject(json.getJSONObject("contents").getJSONArray("quotes").getString(0));

        String quoteOfTheDay = quote.get("quote").toString();
        String timestamp = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now());
        String author = quote.get("author").toString();
        String language = quote.get("language").toString();
        String image = quote.get("background").toString();
        String permalink = json.getJSONObject("copyright").get("url").toString();

        if (ip == fetchClientIpAddr()){
            numberOfCalls += 1;
        }

        //missing numberOfCalls
        return new Quote(quoteOfTheDay, timestamp, numberOfCalls, author, language, image, permalink);
//        Quote(String quoteOfTheDay, String timestamp, int numberOfCalls, String author, String language, String image, String permalink)

    }
}