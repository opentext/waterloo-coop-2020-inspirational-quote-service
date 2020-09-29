package com.opentext.waterloo.coop.inspirationalquoteservice;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class SimpleQuoteRepository implements QuoteRepository{

    @Override
    @Cacheable("Quote")
    public Quote getByQuote(String quoteOfTheDay) {
        simulateSlowService();
        return new Quote(quoteOfTheDay, "Some Quote", 1, "Benjamin", "English", "", "");
    }

    private void simulateSlowService(){
        try{
            long time = 3000L;
            Thread.sleep(time);
        }catch(InterruptedException e){
            throw new IllegalStateException(e);
        }
    }
}
