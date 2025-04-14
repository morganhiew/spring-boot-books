package com.morgan.book.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.morgan.book.model.Book;
import com.morgan.book.service.BookService;

@RestController
public class BookController {

  @Autowired
  private BookService bookService;

  @PostMapping("/books")
  public Book postBook(@RequestBody Book book) {
    Book createdBook = bookService.create(book);
    return createdBook;
  }

  @GetMapping("/books")
  public List<Book> getBooks(@RequestParam(required = false) String author,
      @RequestParam(required = false) Boolean published) {
    return bookService.list(author, published);
  }

  @DeleteMapping("/books/{id}")
  public void deleteBook(@PathVariable Long id) {
    bookService.delete(id);
  }

}
