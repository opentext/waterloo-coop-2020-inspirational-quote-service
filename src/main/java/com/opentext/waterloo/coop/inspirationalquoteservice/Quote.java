package com.opentext.waterloo.coop.inspirationalquoteservice;

public class Quote {

    private final String quoteOfTheDay;
    private final String timestamp;
    private final int numberOfCalls;
    private final String author;
    private final String language;
    private final String image;
    private final String permalink;

    public Quote(String quoteOfTheDay, String timestamp, int numberOfCalls, String author, String language, String image, String permalink) {
        this.quoteOfTheDay = quoteOfTheDay;
        this.timestamp = timestamp;
        this.numberOfCalls = numberOfCalls;
        this.author = author;
        this.language = language;
        this.image = image;
        this.permalink = permalink;
    }

    public String getQuoteOfTheDay() {
        return quoteOfTheDay;
    }
    public String getTimestamp() {
        return timestamp;
    }
    public int getNumberOfCalls() {
        return numberOfCalls;
    }
    public String getAuthor() {
        return author;
    }
    public String getLanguage() {
        return language;
    }
    public String getImage() {
        return image;
    }
    public String getPermalink() {
        return permalink;
    }

    @Override
    public String toString() {
        return "Quote{" +
                "quoteOfTheDay='" + quoteOfTheDay + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", numberOfCalls=" + numberOfCalls +
                ", author='" + author + '\'' +
                ", language='" + language + '\'' +
                ", image='" + image + '\'' +
                ", permalink='" + permalink + '\'' +
                '}';
    }
}