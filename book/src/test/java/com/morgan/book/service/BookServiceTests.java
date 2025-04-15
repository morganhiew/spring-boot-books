package com.morgan.book.service;

import static org.mockito.Mockito.lenient;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;

import com.morgan.book.model.Book;
import com.morgan.book.repository.BookRepository;

@ExtendWith(MockitoExtension.class)
public class BookServiceTests {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @Nested
    class createTests {

        @BeforeEach
        public void setup() {
            Book testBookNormal1 = new Book();
            testBookNormal1.setId((long) 42);
            testBookNormal1.setAuthor("Arthur");
            testBookNormal1.setTitle("Great Expectations");
            testBookNormal1.setPublished(true);
            Mockito.when(bookRepository.save(ArgumentMatchers
                    .argThat(book -> book.getId() == null
                            && book.getPublished().equals(true)
                            && book.getAuthor().equals("Arthur")
                            && book.getTitle().equals("Great Expectations"))))
                    .thenReturn(testBookNormal1);
        }

        @Test
        public void saveBook() throws Exception {
            Book bookInput = new Book("Great Expectations", "Arthur", true);
            Book createdBook = bookService.create(bookInput);
            Assertions.assertEquals((long) 42, createdBook.getId());
            Assertions.assertEquals(bookInput.getAuthor(), createdBook.getAuthor());
            Assertions.assertEquals(bookInput.getPublished(), createdBook.getPublished());
            Assertions.assertEquals(bookInput.getTitle(), createdBook.getTitle());
        }

    }

    @Nested
    class listTests {

        @BeforeEach
        public void setup() {
            Book testBookNormal1 = new Book();
            testBookNormal1.setId((long) 42);
            testBookNormal1.setAuthor("Arthur");
            testBookNormal1.setTitle("Great Expectations");
            testBookNormal1.setPublished(true);
            Book testBookNormal2 = new Book();
            testBookNormal2.setId((long) 43);
            testBookNormal2.setAuthor("Bob");
            testBookNormal2.setTitle("Builder Book");
            testBookNormal2.setPublished(false);

            lenient().when(bookRepository
                    .findAll(Mockito.<Example<Book>>argThat(bookExample -> bookExample != null
                            && bookExample.getProbe() != null
                            && bookExample.getProbe().getId() == null
                            && bookExample.getProbe().getPublished() == null
                            && bookExample.getProbe().getAuthor() == null
                            && bookExample.getProbe().getTitle() == null)))
                    .thenReturn(java.util.Arrays.asList(testBookNormal1, testBookNormal2));

            lenient().when(bookRepository
                    .findAll(Mockito.<Example<Book>>argThat(bookExample -> bookExample != null
                            && bookExample.getProbe() != null
                            && bookExample.getProbe().getId() == null
                            && bookExample.getProbe().getPublished() == null
                            && "AuthorFoo".equals(bookExample.getProbe().getAuthor())
                            && bookExample.getProbe().getTitle() == null)))
                    .thenReturn(java.util.Arrays.asList(testBookNormal1));

            lenient().when(bookRepository
                    .findAll(Mockito.<Example<Book>>argThat(bookExample -> bookExample != null
                            && bookExample.getProbe() != null
                            && bookExample.getProbe().getId() == null
                            && Boolean.TRUE.equals(bookExample.getProbe().getPublished())
                            && bookExample.getProbe().getAuthor() == null
                            && bookExample.getProbe().getTitle() == null)))
                    .thenReturn(java.util.Arrays.asList(testBookNormal2));

            lenient().when(bookRepository
                    .findAll(Mockito.<Example<Book>>argThat(bookExample -> bookExample != null
                            && bookExample.getProbe() != null
                            && bookExample.getProbe().getId() == null
                            && Boolean.TRUE.equals(bookExample.getProbe().getPublished())
                            && "AuthorFoo".equals(bookExample.getProbe().getAuthor())
                            && bookExample.getProbe().getTitle() == null)))
                    .thenReturn(java.util.Arrays.asList(testBookNormal2, testBookNormal2));

        }

        @Test
        public void authorNull_publishedNull() throws Exception {
            List<Book> books = bookService.list(null, null);
            Assertions.assertEquals((long) 42, books.get(0).getId());
            Assertions.assertEquals("Arthur", books.get(0).getAuthor());
            Assertions.assertEquals(true, books.get(0).getPublished());
            Assertions.assertEquals("Great Expectations", books.get(0).getTitle());
            Assertions.assertEquals((long) 43, books.get(1).getId());
            Assertions.assertEquals("Bob", books.get(1).getAuthor());
            Assertions.assertEquals(false, books.get(1).getPublished());
            Assertions.assertEquals("Builder Book", books.get(1).getTitle());
        }

        @Test
        public void authorValid_publishedNull() throws Exception {
            List<Book> books = bookService.list("AuthorFoo", null);
            Assertions.assertEquals((long) 42, books.get(0).getId());
            Assertions.assertEquals("Arthur", books.get(0).getAuthor());
            Assertions.assertEquals(true, books.get(0).getPublished());
            Assertions.assertEquals("Great Expectations", books.get(0).getTitle());
        }

        @Test
        public void authorNull_publishedValid() throws Exception {
            List<Book> books = bookService.list(null, true);
            Assertions.assertEquals((long) 43, books.get(0).getId());
            Assertions.assertEquals("Bob", books.get(0).getAuthor());
            Assertions.assertEquals(false, books.get(0).getPublished());
            Assertions.assertEquals("Builder Book", books.get(0).getTitle());
        }

        @Test
        public void authorValid_publishedValid() throws Exception {
            List<Book> books = bookService.list("AuthorFoo", true);
            Assertions.assertEquals((long) 43, books.get(0).getId());
            Assertions.assertEquals("Bob", books.get(0).getAuthor());
            Assertions.assertEquals(false, books.get(0).getPublished());
            Assertions.assertEquals("Builder Book", books.get(0).getTitle());
            Assertions.assertEquals((long) 43, books.get(1).getId());
            Assertions.assertEquals("Bob", books.get(1).getAuthor());
            Assertions.assertEquals(false, books.get(1).getPublished());
            Assertions.assertEquals("Builder Book", books.get(1).getTitle());
        }

    }

    @Nested
    class deleteTests {

        @BeforeEach
        public void setup() {
            Mockito.doNothing().when(bookRepository).deleteById((long) 42);
        }

        @Test
        public void deleteCalled() throws Exception {
            bookService.delete((long) 42);
            Mockito.verify(bookRepository, Mockito.times(1)).deleteById((long) 42);
        }

    }

}