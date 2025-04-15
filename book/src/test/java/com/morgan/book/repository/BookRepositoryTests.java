package com.morgan.book.repository;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
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
                Book book = new Book("Title" + String.valueOf(i), "Author" + String.valueOf(i % 2),
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
                Assertions.assertEquals("Author" + String.valueOf(i % 2), book.getAuthor());
                Assertions.assertEquals(i % 2 == 0, book.getPublished());
            }
        }

        @Test // should find Title1, Title3
        public void findWithAuthorOnlyBookExample() throws Exception {
            ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues();
            Book authorOnlyBookExample = new Book();
            authorOnlyBookExample.setAuthor("Author1");
            Example<Book> exampleQuery = Example.of(authorOnlyBookExample, matcher);
            List<Book> books = bookRepository.findAll(exampleQuery);
            Assertions.assertEquals(2, books.size());
            Book book1 = books.get(0);
            Assertions.assertNotNull(book1.getId());
            Assertions.assertEquals("Title1", book1.getTitle());
            Assertions.assertEquals("Author1", book1.getAuthor());
            Assertions.assertEquals(false, book1.getPublished());
            Book book2 = books.get(1);
            Assertions.assertNotNull(book2.getId());
            Assertions.assertEquals("Title3", book2.getTitle());
            Assertions.assertEquals("Author1", book2.getAuthor());
            Assertions.assertEquals(false, book2.getPublished());
        }

        @Test // should find Title1, Title3
        public void findWithAuthorOnlyBookExampleEmptyResult() throws Exception {
            ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues();
            Book authorOnlyBookExample = new Book();
            authorOnlyBookExample.setAuthor("Author3");
            Example<Book> exampleQuery = Example.of(authorOnlyBookExample, matcher);
            List<Book> books = bookRepository.findAll(exampleQuery);
            Assertions.assertEquals(0, books.size());
        }

        @Test // should find Title0, Title2, Title4
        public void findWithPublichedOnlyBookExample() throws Exception {
            ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues();
            Book publishedOnlyBookExample = new Book();
            publishedOnlyBookExample.setPublished(true);
            Example<Book> exampleQuery = Example.of(publishedOnlyBookExample, matcher);
            List<Book> books = bookRepository.findAll(exampleQuery);
            Assertions.assertEquals(3, books.size());
            for (int i = 0; i < 3; i++) {
                Book book = books.get(i);
                Assertions.assertNotNull(book.getId());
                Assertions.assertEquals("Title" + String.valueOf(i*2), book.getTitle());
                Assertions.assertEquals("Author0", book.getAuthor());
                Assertions.assertEquals(true, book.getPublished());
            }
        }

        @Test // should find Title0, Title2, Title4
        public void findWithFullBookExample() throws Exception {
            ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues();
            Book fullBookExample = new Book();
            fullBookExample.setAuthor("Author0");
            fullBookExample.setPublished(true);
            Example<Book> exampleQuery = Example.of(fullBookExample, matcher);
            List<Book> books = bookRepository.findAll(exampleQuery);
            Assertions.assertEquals(3, books.size());
            for (int i = 0; i < 3; i++) {
                Book book = books.get(i);
                Assertions.assertNotNull(book.getId());
                Assertions.assertEquals("Title" + String.valueOf(i*2), book.getTitle());
                Assertions.assertEquals("Author0", book.getAuthor());
                Assertions.assertEquals(true, book.getPublished());
            }
        }

        @Test // should find no title
        public void findWithFullBookExampleEmptyResult() throws Exception {
            ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues();
            Book fullBookExample = new Book();
            fullBookExample.setAuthor("Author0");
            fullBookExample.setPublished(false);
            Example<Book> exampleQuery = Example.of(fullBookExample, matcher);
            List<Book> books = bookRepository.findAll(exampleQuery);
            Assertions.assertEquals(0, books.size());
        }

    }

    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    class deleteTests {

        @BeforeEach
        public void setup() {
            bookRepository.deleteAll();
            for (int i = 0; i < 5; i++) {
                Book book = new Book("Title" + String.valueOf(i), "Author" + String.valueOf(i % 2),
                        i % 2 == 0);
                bookRepository.save(book);
            }
        }

        @Test
        public void deleteExistingBook() throws Exception {
            List<Book> books = bookRepository.findAll();
            Book firstBook = books.getFirst();
            bookRepository.deleteById(firstBook.getId());
            List<Book> updatedBooks = bookRepository.findAll();
            Assertions.assertEquals(4, updatedBooks.size());
            for (int i = 1; i < 5; i++) {
                Book book = updatedBooks.get(i - 1);
                Assertions.assertNotNull(book.getId());
                Assertions.assertEquals("Title" + String.valueOf(i), book.getTitle());
                Assertions.assertEquals("Author" + String.valueOf(i % 2), book.getAuthor());
                Assertions.assertEquals(i % 2 == 0, book.getPublished());
            }
        }

        @Test // deleting by an inexisting book id will be silently ignored
        public void deleteInexistingBook() throws Exception {
            bookRepository.deleteById((long) -1);
            List<Book> books = bookRepository.findAll();
            Assertions.assertEquals(5, books.size());
            for (int i = 0; i < 5; i++) {
                Book book = books.get(i);
                Assertions.assertNotNull(book.getId());
                Assertions.assertEquals("Title" + String.valueOf(i), book.getTitle());
                Assertions.assertEquals("Author" + String.valueOf(i % 2), book.getAuthor());
                Assertions.assertEquals(i % 2 == 0, book.getPublished());
            }
        }

        @Test
        public void nullBookId() throws Exception {
            Assertions.assertThrows(InvalidDataAccessApiUsageException.class, () -> {
                bookRepository.deleteById(null);
            });
        }

    }

}