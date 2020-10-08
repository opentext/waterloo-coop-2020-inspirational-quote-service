package com.opentext.waterloo.coop.inspirationalquoteservice;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class InspirationalQuoteServiceApplicationTests {

    private static final Logger log = LoggerFactory.getLogger(InspirationalQuoteServiceApplicationTests.class);
    private final RemoteQuoteRepository remoteQuoteRepository = new RemoteQuoteRepository();
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

    @Test
    @Profile("dev")
    public void overwriteJsonFileFromLiveQuote() {
        //try to fetch online api quote
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);

            String originalJson = remoteQuoteRepository.fetchJSON().toString();
            JsonNode tree = objectMapper .readTree(originalJson);
            String formattedJson = objectMapper.writeValueAsString(tree);

            File file = new File("src/main/resources/plain.json");
            BufferedWriter br = new BufferedWriter(new FileWriter(file));
            br.write(formattedJson);
            br.flush();
            br.close();
        } catch (Exception e) {
            log.error("Update json file failed! Aborting..." + e.getMessage());
        }
    }
}
