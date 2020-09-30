package com.opentext.waterloo.coop.inspirationalquoteservice;


import org.json.JSONObject;

import org.junit.jupiter.api.Test;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;


import static org.mockito.Mockito.doReturn;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RemoteRepoTest {

    @Mock
    RemoteQuoteRepository remoteQuoteRepository = Mockito.mock(RemoteQuoteRepository.class);
    JSONObject mockJSON = Mockito.mock(JSONObject.class);

    @Test public void remoteTest() {
        try {
            doReturn(mockJSON).when(remoteQuoteRepository).fetchJSON();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
