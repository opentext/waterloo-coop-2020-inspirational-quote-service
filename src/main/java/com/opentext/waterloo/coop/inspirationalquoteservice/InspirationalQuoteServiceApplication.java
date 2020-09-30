package com.opentext.waterloo.coop.inspirationalquoteservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching(proxyTargetClass = true)
@EnableScheduling
public class InspirationalQuoteServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InspirationalQuoteServiceApplication.class, args);
    }

}
