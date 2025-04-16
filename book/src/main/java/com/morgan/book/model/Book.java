package com.morgan.book.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Pattern(regexp = "[a-zA-Z0-9]+", message = "title must be alphanumeric")
    private String title;

    @NotNull
    @Pattern(regexp = "[a-zA-Z0-9]+", message = "author must be alphanumeric")
    private String author;

    @NotNull
    private Boolean published;

    // Getters and Setters
    public Book(String title, String author, Boolean published) {
        this.title = title;
        this.author = author;
        this.published = published;
    }
}
