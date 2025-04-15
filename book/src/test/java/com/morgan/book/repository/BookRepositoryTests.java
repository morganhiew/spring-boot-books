package com.morgan.book.repository;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.test.context.TestPropertySource;

import com.morgan.book.model.Book;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-unittest.properties")
public class BookRepositoryTests {

        @Autowired
        private BookRepository bookRepository;

        @Nested
        class saveTests {
                @Test
                public void normalCase() throws Exception {
                        Book book = new Book("Sometitle", "Someauthor", true);
                        Book savedBook = bookRepository.save(book);
                        Assertions.assertNotNull(savedBook.getId());
                        Optional<Book> foundBook = bookRepository.findById(savedBook.getId());
                        Assertions.assertTrue(foundBook.isPresent());
                        Assertions.assertEquals(savedBook.getId(), foundBook.orElseThrow().getId());
                        Assertions.assertEquals(savedBook.getAuthor(), foundBook.orElseThrow().getAuthor());
                        Assertions.assertEquals(savedBook.getPublished(), foundBook.orElseThrow().getPublished());
                        Assertions.assertEquals(savedBook.getTitle(), foundBook.orElseThrow().getTitle());
                }

        }

        @Nested
        @TestInstance(Lifecycle.PER_CLASS)
        class findAllTests {

                @BeforeAll
                public void setup() {
                        bookRepository.deleteAll();
                        for (int i = 0; i < 5; i++) {
                                Book book = new Book("Title" + String.valueOf(i), "Author" + String.valueOf(i),
                                                i % 2 == 0);
                                bookRepository.save(book);
                        }
                }

                @Test
                public void findWithEmptyBookExample() throws Exception {
                        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues();
                        Book emptyBookExample = new Book();
                        Example<Book> exampleQuery = Example.of(emptyBookExample, matcher);
                        List<Book> books = bookRepository.findAll(exampleQuery);
                        Assertions.assertEquals(5, books.size());
                        for (int i = 0; i < 5; i++) {
                                Book book = books.get(i);
                                Assertions.assertNotNull(book.getId());
                                Assertions.assertEquals("Title" + String.valueOf(i), book.getTitle());
                                Assertions.assertEquals("Author" + String.valueOf(i), book.getAuthor());
                                Assertions.assertEquals(i % 2 == 0, book.getPublished());
                        }
                }

        }

        @Nested
        class deleteTests {
                @Test
                public void normalCase() throws Exception {
                }

        }

}