package com.morgan.book.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import com.morgan.book.model.Book;
import com.morgan.book.repository.BookRepository;

@Service
public class BookService {

  @Autowired
  private BookRepository bookRepository;

  public Book create(Book book) {
    return bookRepository.save(book);
  }

  public List<Book> list(String author, Boolean published) {
    ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues();
    Book bookExample = new Book();
    bookExample.setAuthor(author);
    bookExample.setPublished(published);
    Example<Book> exampleQuery = Example.of(bookExample, matcher);
    return bookRepository.findAll(exampleQuery);
  }

  public void delete(Long id) {
    bookRepository.deleteById(id);
    return;
  }

}
