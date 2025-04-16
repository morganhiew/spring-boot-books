package com.morgan.book.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.morgan.book.model.Book;
import com.morgan.book.service.BookService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = BookController.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-unittest.properties")
@EnableWebMvc
public class BookControllerTests {

        @Autowired
        private MockMvc mvc;
        @MockitoBean
        private BookService bookService;

        @BeforeEach
        public void setup() {
                Book testBookNormal1 = new Book();
                testBookNormal1.setId(42L);
                testBookNormal1.setAuthor("Arthur");
                testBookNormal1.setTitle("Great Expectations");
                testBookNormal1.setPublished(true);
                Book testBookNormal2 = new Book();
                testBookNormal2.setId(43L);
                testBookNormal2.setAuthor("Bob");
                testBookNormal2.setTitle("Builder Book");
                testBookNormal2.setPublished(false);
                Mockito.when(bookService.create(ArgumentMatchers
                                .argThat(book -> book.getId() == null
                                                && book.getPublished().equals(true)
                                                && book.getAuthor().equals("Arthur")
                                                && book.getTitle().equals("Great Expectations"))))
                                .thenReturn(testBookNormal1);
                Mockito.when(bookService.list(null, null))
                                .thenReturn(java.util.Arrays.asList(testBookNormal1, testBookNormal2));
        }

        @Nested
        class postBookTests {
                @Test
                public void postBook() throws Exception {
                        ResultActions testCase = mvc.perform(post("/books")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content("{\"author\":\"Arthur\",\"title\":\"Great Expectations\",\"published\":true}"));
                        testCase.andExpect(status().isOk())
                                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                        .andExpect(jsonPath("$.id").value(42))
                                        .andExpect(jsonPath("$.author").value("Arthur"))
                                        .andExpect(jsonPath("$.title").value("Great Expectations"))
                                        .andExpect(jsonPath("$.published").value(true));

                }

                @Test
                public void contentTypeNotJson() throws Exception {
                        ResultActions testCase = mvc.perform(post("/books")
                                        .contentType(MediaType.TEXT_PLAIN)
                                        .content("{\"author\":\"Arthur\",\"title\":\"Great Expectations\",\"published\":true}"));
                        testCase.andExpect(status().isUnsupportedMediaType());
                }

        }

        @Nested
        class getBooksTests {
                @Test
                public void getBooks() throws Exception {
                        ResultActions testCase = mvc.perform(get("/books"));
                        testCase.andExpect(status().isOk())
                                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                        .andExpect(jsonPath("$[0].id").value(42))
                                        .andExpect(jsonPath("$[0].author").value("Arthur"))
                                        .andExpect(jsonPath("$[0].title").value("Great Expectations"))
                                        .andExpect(jsonPath("$[0].published").value(true))
                                        .andExpect(jsonPath("$[1].id").value(43))
                                        .andExpect(jsonPath("$[1].author").value("Bob"))
                                        .andExpect(jsonPath("$[1].title").value("Builder Book"))
                                        .andExpect(jsonPath("$[1].published").value(false));
                }

        }

        @Nested
        class deleteBookTests {

                @Test
                public void deleteBook() throws Exception {
                        ResultActions testCase = mvc.perform(delete("/books/56"));
                        testCase.andExpect(status().isOk());
                        Mockito.verify(bookService, Mockito.times(1)).delete(56L);
                }

        }

}