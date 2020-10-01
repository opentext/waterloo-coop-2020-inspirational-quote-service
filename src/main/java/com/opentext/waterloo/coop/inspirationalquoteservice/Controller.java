package com.opentext.waterloo.coop.inspirationalquoteservice;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Hashtable;
import java.util.Optional;

@RestController
public class Controller {
    static int numberOfCalls = 0;
    static Hashtable<String, Integer> ip_address = new Hashtable<String, Integer>();
    Globalcounter
    @Autowired
    private final QuoteRepository localQuoteRepository;
    @Autowired
    private final QuoteRepository remoteQuoteRepository;

    public Controller(QuoteRepository localQuoteRepository, QuoteRepository remoteQuoteRepository) {
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

    public JSONObject remoteFetchJSON() throws Exception {
        return remoteQuoteRepository.fetchJSON();
    }
    public JSONObject localFetchJSON() throws Exception {
        return localQuoteRepository.fetchJSON();
    }

    @GetMapping("/quote")
    public Quote quote() throws Exception {

        JSONObject json;
        String ip = fetchClientIpAddr();
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
        ip_address.put(ip,numberOfCalls);
        if (ip == fetchClientIpAddr()){
            numberOfCalls += 1;
        }

        //missing numberOfCalls
        return new Quote(quoteOfTheDay, timestamp, ip_address.get(fetchClientIpAddr()), author, language, image, permalink);
//        Quote(String quoteOfTheDay, String timestamp, int numberOfCalls, String author, String language, String image, String permalink)

    }
}