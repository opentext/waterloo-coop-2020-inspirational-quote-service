package com.opentext.waterloo.coop.inspirationalquoteservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureMockMvc
public class LocalRepoTest {

    @Autowired
    private LocalQuoteRepository localQuoteRepository;

    @Test
    public void contextLoads() {
        assertThat(localQuoteRepository).isNotNull();
    }

}
