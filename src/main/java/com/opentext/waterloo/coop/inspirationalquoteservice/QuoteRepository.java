package com.opentext.waterloo.coop.inspirationalquoteservice;

public interface QuoteRepository{

    Quote getByQuote(String quoteOfTheDay);
}
