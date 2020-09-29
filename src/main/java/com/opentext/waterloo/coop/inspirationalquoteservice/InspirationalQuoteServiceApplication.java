package com.opentext.waterloo.coop.inspirationalquoteservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class InspirationalQuoteServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InspirationalQuoteServiceApplication.class, args);
    }

}
