package com.morgan.book.model;

import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@TestInstance(Lifecycle.PER_CLASS)
public class BookValidationTests {
    private Validator validator;

    @BeforeAll
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Nested
    class titleTests {
        @Test
        public void validTitle() {
            Book book = new Book();
            book.setTitle("Foobar");
            book.setAuthor("Author");
            book.setPublished(true);
            Set<ConstraintViolation<Book>> violations = validator.validate(book);
            Assertions.assertTrue(violations.isEmpty());
        }

        @Test
        public void invalidTitleDot() {
            Book book = new Book();
            book.setTitle("Foobar.");
            book.setAuthor("Author");
            book.setPublished(true);
            Set<ConstraintViolation<Book>> violations = validator.validate(book);
            Assertions.assertEquals(1, violations.size());
        }

        @Test
        public void invalidTitleSpace() {
            Book book = new Book();
            book.setTitle(" Foobar");
            book.setAuthor("Author");
            book.setPublished(true);
            Set<ConstraintViolation<Book>> violations = validator.validate(book);
            Assertions.assertEquals(1, violations.size());
        }

        @Test
        public void missingTitle() {
            Book book = new Book();
            book.setAuthor("Author");
            book.setPublished(true);
            Set<ConstraintViolation<Book>> violations = validator.validate(book);
            Assertions.assertEquals(1, violations.size());
        }
    }

    @Nested
    class authorTests {
        @Test
        public void validAuthor() {
            Book book = new Book();
            book.setTitle("Foobar");
            book.setAuthor("Author");
            book.setPublished(true);
            Set<ConstraintViolation<Book>> violations = validator.validate(book);
            Assertions.assertTrue(violations.isEmpty());
        }

        @Test
        public void invalidAuthorDot() {
            Book book = new Book();
            book.setTitle("Foobar");
            book.setAuthor(".Author");
            book.setPublished(true);
            Set<ConstraintViolation<Book>> violations = validator.validate(book);
            Assertions.assertEquals(1, violations.size());
        }

        @Test
        public void invalidAuthorSpace() {
            Book book = new Book();
            book.setTitle("Foobar");
            book.setAuthor("Author ");
            book.setPublished(true);
            Set<ConstraintViolation<Book>> violations = validator.validate(book);
            Assertions.assertEquals(1, violations.size());
        }

        @Test
        public void missingAuthor() {
            Book book = new Book();
            book.setTitle("Foobar");
            book.setPublished(true);
            Set<ConstraintViolation<Book>> violations = validator.validate(book);
            Assertions.assertEquals(1, violations.size());
        }
    }

    @Nested
    class publishedTests {
        @Test
        public void validPublished() {
            Book book = new Book();
            book.setTitle("Foobar");
            book.setAuthor("Author");
            book.setPublished(true);
            Set<ConstraintViolation<Book>> violations = validator.validate(book);
            Assertions.assertTrue(violations.isEmpty());
        }

        @Test
        public void missingPublished() {
            Book book = new Book();
            book.setTitle("Foobar");
            book.setAuthor("Author");
            Set<ConstraintViolation<Book>> violations = validator.validate(book);
            Assertions.assertEquals(1, violations.size());
        }
    }

    @Nested
    class combinationTests {
        @Test
        public void validTitle_validAuthor_validPublished() {
            Book book = new Book();
            book.setTitle("Foobar");
            book.setAuthor("Author");
            book.setPublished(true);
            Set<ConstraintViolation<Book>> violations = validator.validate(book);
            Assertions.assertTrue(violations.isEmpty());
        }

        @Test
        public void invalidTitle_validAuthor_validPublished() {
            Book book = new Book();
            book.setTitle("T.Foobar");
            book.setAuthor("Author");
            book.setPublished(true);
            Set<ConstraintViolation<Book>> violations = validator.validate(book);
            Assertions.assertEquals(1, violations.size());
        }

        @Test
        public void missingTitle_validAuthor_validPublished() {
            Book book = new Book();
            book.setAuthor("Author");
            book.setPublished(true);
            Set<ConstraintViolation<Book>> violations = validator.validate(book);
            Assertions.assertEquals(1, violations.size());
        }

        @Test
        public void validTitle_invalidAuthor_validPublished() {
            Book book = new Book();
            book.setTitle("Foobar");
            book.setAuthor("Author!");
            book.setPublished(true);
            Set<ConstraintViolation<Book>> violations = validator.validate(book);
            Assertions.assertEquals(1, violations.size());
        }

        @Test
        public void invalidTitle_invalidAuthor_validPublished() {
            Book book = new Book();
            book.setTitle("T.Foobar");
            book.setAuthor("Author!");
            book.setPublished(true);
            Set<ConstraintViolation<Book>> violations = validator.validate(book);
            Assertions.assertEquals(2, violations.size());
        }

        @Test
        public void missingTitle_invalidAuthor_validPublished() {
            Book book = new Book();
            book.setAuthor("Author!");
            book.setPublished(true);
            Set<ConstraintViolation<Book>> violations = validator.validate(book);
            Assertions.assertEquals(2, violations.size());
        }

        @Test
        public void validTitle_missingAuthor_validPublished() {
            Book book = new Book();
            book.setTitle("Foobar");
            book.setPublished(true);
            Set<ConstraintViolation<Book>> violations = validator.validate(book);
            Assertions.assertEquals(1, violations.size());
        }

        @Test
        public void invalidTitle_missingAuthor_validPublished() {
            Book book = new Book();
            book.setTitle("T.Foobar");
            book.setPublished(true);
            Set<ConstraintViolation<Book>> violations = validator.validate(book);
            Assertions.assertEquals(2, violations.size());
        }

        @Test
        public void missingTitle_missingAuthor_validPublished() {
            Book book = new Book();
            book.setPublished(true);
            Set<ConstraintViolation<Book>> violations = validator.validate(book);
            Assertions.assertEquals(2, violations.size());
        }

        @Test
        public void validTitle_validAuthor_missingPublished() {
            Book book = new Book();
            book.setTitle("Foobar");
            book.setAuthor("Author");
            Set<ConstraintViolation<Book>> violations = validator.validate(book);
            Assertions.assertEquals(1, violations.size());
        }

        @Test
        public void invalidTitle_validAuthor_missingPublished() {
            Book book = new Book();
            book.setTitle("T.Foobar");
            book.setAuthor("Author");
            Set<ConstraintViolation<Book>> violations = validator.validate(book);
            Assertions.assertEquals(2, violations.size());
        }

        @Test
        public void missingTitle_validAuthor_missingPublished() {
            Book book = new Book();
            book.setAuthor("Author");
            Set<ConstraintViolation<Book>> violations = validator.validate(book);
            Assertions.assertEquals(2, violations.size());
        }

        @Test
        public void validTitle_invalidAuthor_missingPublished() {
            Book book = new Book();
            book.setTitle("Foobar");
            book.setAuthor("Author!");
            Set<ConstraintViolation<Book>> violations = validator.validate(book);
            Assertions.assertEquals(2, violations.size());
        }

        @Test
        public void invalidTitle_invalidAuthor_missingPublished() {
            Book book = new Book();
            book.setTitle("T.Foobar");
            book.setAuthor("Author!");
            Set<ConstraintViolation<Book>> violations = validator.validate(book);
            Assertions.assertEquals(3, violations.size());
        }

        @Test
        public void missingTitle_invalidAuthor_missingPublished() {
            Book book = new Book();
            book.setAuthor("Author!");
            Set<ConstraintViolation<Book>> violations = validator.validate(book);
            Assertions.assertEquals(3, violations.size());
        }

        @Test
        public void validTitle_missingAuthor_missingPublished() {
            Book book = new Book();
            book.setTitle("Foobar");
            Set<ConstraintViolation<Book>> violations = validator.validate(book);
            Assertions.assertEquals(2, violations.size());
        }

        @Test
        public void invalidTitle_missingAuthor_missingPublished() {
            Book book = new Book();
            book.setTitle("T.Foobar");
            Set<ConstraintViolation<Book>> violations = validator.validate(book);
            Assertions.assertEquals(3, violations.size());
        }

        @Test
        public void missingTitle_missingAuthor_missingPublished() {
            Book book = new Book();
            Set<ConstraintViolation<Book>> violations = validator.validate(book);
            Assertions.assertEquals(3, violations.size());
        }
    }

}
