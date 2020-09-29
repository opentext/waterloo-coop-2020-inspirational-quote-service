package com.opentext.waterloo.coop.inspirationalquoteservice;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AppRunner implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(AppRunner.class);

    private final QuoteRepository quoteRepository;

    public AppRunner(QuoteRepository quoteRepository){
        this.quoteRepository = quoteRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info(".... Fetching quotes");
//        logger.info("isbn-1234 -->" + QuoteRepository.getByQuote("isbn-1234"));
//        logger.info("isbn-4567 -->" + QuoteRepository.getByQuote("isbn-4567"));
//        logger.info("isbn-1234 -->" + QuoteRepository.getByQuote("isbn-1234"));
//        logger.info("isbn-4567 -->" + QuoteRepository.getByQuote("isbn-4567"));
//        logger.info("isbn-1234 -->" + QuoteRepository.getByQuote("isbn-1234"));
//        logger.info("isbn-1234 -->" + QuoteRepository.getByQuote("isbn-1234"));
    }
}
