package com.opentext.waterloo.coop.inspirationalquoteservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class InspirationalQuoteServiceApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnJson() throws Exception {
        this.mockMvc.perform(get("/quote"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.quoteOfTheDay").isString())
                    .andExpect(jsonPath("$.timestamp").isString())
                    .andExpect(jsonPath("$.numberOfCalls").isNumber())
                    .andExpect(jsonPath("$.author").isString())
                    .andExpect(jsonPath("$.language").isString())
                    .andExpect(jsonPath("$.image").isString())
                    .andExpect(jsonPath("$.permalink").isString());

    }


}
