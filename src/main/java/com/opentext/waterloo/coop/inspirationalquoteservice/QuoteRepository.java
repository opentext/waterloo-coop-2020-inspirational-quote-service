package com.opentext.waterloo.coop.inspirationalquoteservice;

public interface QuoteRepository{

    Quote getByQuote(String quoteOfTheDay, String timeStamp,
                     int numberOfCalls, String author, String language, String image, String permalink);
}
