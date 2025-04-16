package com.morgan.book.controller.exception;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-unittest.properties")
@EnableWebMvc
public class GlobalExceptionHandlerTests {

    @Autowired
    private MockMvc mvc;

    @Nested
    class validationExceptionTests {
        @Test
        public void validationExceptionHandling() throws Exception {
            ResultActions testCase = mvc.perform(post("/books")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"author\":\"Arthur!\",\"title\":\"GreatExpectations\",\"published\":true}"));
            testCase.andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.timestamp").exists())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.error").value("BAD_REQUEST"))
                    .andExpect(jsonPath("$.message").value("Constraint Violation"))
                    .andExpect(jsonPath("$.details").value("{author=author must be alphanumeric}"))
                    .andExpect(jsonPath("$.path").exists()); // mock mvc does not provide path
        }

    }

    @Nested // check that exceptions that are not intentionally handled are still raised in
            // expected manner
    class otherExceptionTests {
        @Test
        public void unsupportedMediaType() throws Exception {
            ResultActions testCase = mvc.perform(post("/books")
                    .contentType(MediaType.TEXT_PLAIN)
                    .content("{\"author\":\"Arthur\",\"title\":\"Great Expectations\",\"published\":true}"));
            testCase.andExpect(status().isUnsupportedMediaType());
        }

    }

}