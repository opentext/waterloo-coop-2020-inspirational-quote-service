package com.opentext.waterloo.coop.inspirationalquoteservice;


import org.json.JSONObject;

public interface QuoteRepository {
    JSONObject fetchJSON() throws Exception;
}
