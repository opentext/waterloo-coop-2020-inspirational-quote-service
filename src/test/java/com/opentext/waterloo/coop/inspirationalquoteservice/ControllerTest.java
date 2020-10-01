package com.opentext.waterloo.coop.inspirationalquoteservice;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = InspirationalQuoteServiceApplication.class)
@WebAppConfiguration
public class ControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    private static final String mockIP_1 = "fake_ip_1";
    private static final String mockIP_2 = "fake_ip_2";

    @BeforeEach
    public void setup() {
        this.mockMvc = webAppContextSetup (wac).build();
    }

    @Test
    public void webAppContextTest() throws Exception {
        assertTrue(wac.getServletContext() instanceof MockServletContext);
    }

    @Test
    public void testControllerReturnsDifferentCountForDifferentIPs() throws Exception {

        mockMvc.perform(get("/quote")
                .with(remoteAddr(mockIP_1))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.numberOfCalls").value(1));
//                .andDo(new ResultHandler() {
//                    @Override
//                    public void handle(MvcResult mvcResult) throws Exception {
//                        System.out.println(mvcResult.getRequest().getRemoteAddr());
//                    }
//                });
        mockMvc.perform(get("/quote")
                .with(remoteAddr(mockIP_2))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.numberOfCalls").value(1));
        mockMvc.perform(get("/quote")
                .with(remoteAddr(mockIP_1))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.numberOfCalls").value(2));
        mockMvc.perform(get("/quote")
                .with(remoteAddr(mockIP_2))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.numberOfCalls").value(2));
    }

    @Test
    public void testControllerReturnsIPAddressBasedOnRemoteClient() throws Exception {
        mockMvc.perform(get("/quote")
                .with(remoteAddr(mockIP_1))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(new ResultHandler() {
                    @Override
                    public void handle(MvcResult mvcResult) throws Exception {
                        assertEquals(mockIP_1, mvcResult.getRequest().getRemoteAddr());
                    }
                });
        mockMvc.perform(get("/quote")
                .with(remoteAddr(mockIP_2))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(new ResultHandler() {
                    @Override
                    public void handle(MvcResult mvcResult) throws Exception {
                        assertEquals(mockIP_2, mvcResult.getRequest().getRemoteAddr());
                    }
                });
    }

    private static RequestPostProcessor remoteAddr(final String remoteAddr) { // it's nice to extract into a helper
        return new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setRemoteAddr(remoteAddr);
                return request;
            }
        };
    }
}
