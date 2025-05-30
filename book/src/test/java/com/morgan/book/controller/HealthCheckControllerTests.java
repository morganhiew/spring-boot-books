package com.morgan.book.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = HealthCheckController.class)
@AutoConfigureMockMvc
public class HealthCheckControllerTests {

    @Autowired
    private MockMvc mvc;

    @Test
    public void testHealthCheck() throws Exception {
        ResultActions positiveCase = mvc.perform(get("/health"));
        positiveCase.andExpect(status().isOk())
                .andExpect(content().string("Healthy!"));
    }

}