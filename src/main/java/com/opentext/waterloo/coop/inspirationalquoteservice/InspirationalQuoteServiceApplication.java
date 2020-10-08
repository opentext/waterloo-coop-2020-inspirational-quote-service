package com.opentext.waterloo.coop.inspirationalquoteservice;

import org.apache.log4j.PropertyConfigurator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;

@SpringBootApplication
@EnableCaching(proxyTargetClass = true)
@EnableScheduling
public class InspirationalQuoteServiceApplication {

    public static void main(String[] args) throws IOException {
//        System.setProperty("log4j.defaultInitOverride", "true");
        SpringApplication.run(InspirationalQuoteServiceApplication.class, args);
    }

    @Bean
    @Profile("dev")
    public void devPropertyConfigurator() throws IOException {
        Resource resource = new ClassPathResource("log4j-dev.properties");
        PropertyConfigurator.configure(resource.getURL());
    }
    @Bean
    @Profile("prod")
    public void prodPropertyConfigurator() throws IOException {
        Resource resource = new ClassPathResource("log4j-prod.properties");
        PropertyConfigurator.configure(resource.getURL());
    }
    @Bean
    @Profile("graylog")
    public void graylogPropertyConfigurator() throws IOException {
        Resource resource = new ClassPathResource("log4j-graylog.properties");
        PropertyConfigurator.configure(resource.getURL());
    }
}
