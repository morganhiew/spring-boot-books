package com.morgan.book.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.morgan.book.model.Book;

public interface BookRepository extends JpaRepository<Book, Long>{
  
}
