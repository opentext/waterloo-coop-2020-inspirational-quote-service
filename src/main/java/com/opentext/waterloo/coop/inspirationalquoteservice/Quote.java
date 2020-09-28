package com.opentext.waterloo.coop.inspirationalquoteservice;

public class Quote {

    private final long numOfCalls;
    private final String quoteOfTheDay;

    public Quote(long numOfCalls, String quoteOfTheDay) {
        this.numOfCalls = numOfCalls;
        this.quoteOfTheDay = quoteOfTheDay;
    }

    public long getNumOfCalls() {
        return numOfCalls;
    }

    public String getQuoteOfTheDay() {
        return quoteOfTheDay;
    }
}
