package com.opentext.waterloo.coop.inspirationalquoteservice;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class SimpleQuoteRepository implements QuoteRepository{

    @Override
    @Cacheable("Quote")
    public Quote getByQuote(String quoteOfTheDay, String timeStamp,
                                  int numberOfCalls, String author, String language, String image, String permalink) {
//        simulateSlowService();
        return new Quote(quoteOfTheDay, timeStamp,numberOfCalls, language, language, image, permalink);
    }

//    private void simulateSlowService(){
//        try{
//            long time = 3000L;
//            Thread.sleep(time);
//        }catch(InterruptedException e){
//            throw new IllegalStateException(e);
//        }
//    }
}
