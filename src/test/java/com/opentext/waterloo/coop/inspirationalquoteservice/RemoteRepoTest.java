package com.opentext.waterloo.coop.inspirationalquoteservice;


import org.json.JSONObject;

import org.junit.jupiter.api.Test;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;


import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RemoteRepoTest {

    @Mock
    RemoteQuoteRepository remoteQuoteRepository = Mockito.mock(RemoteQuoteRepository.class);
    JSONObject mockJSON = Mockito.mock(JSONObject.class);

    @Autowired
    CacheManager cacheManager;

    @Autowired
    WebApplicationContext wac;

    private MockMvc mockMvc;

    @Test public void remoteTest() {
        try {
            doReturn(mockJSON).when(remoteQuoteRepository).fetchJSON();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Test if the responses from the theysaidso api are cached properly
    @Test
    public void cacheShouldPopulate() throws Exception{
        mockMvc = webAppContextSetup(wac).build();
        mockMvc.perform(get("/quote"));
        Cache cache = cacheManager.getCache("quote");

        assertTrue(cache != null);
    }


}
