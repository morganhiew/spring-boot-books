package com.morgan.book.repository;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
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

}