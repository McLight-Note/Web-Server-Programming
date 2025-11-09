package com.example.bookswap.repository;

import com.example.bookswap.model.Book;
import com.example.bookswap.model.BookStatus;
import com.example.bookswap.model.Role;
import com.example.bookswap.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BookRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    private User owner;
    private Book book1;
    private Book book2;

    @BeforeEach
    void setUp() {
        owner = new User();
        owner.setName("John Doe");
        owner.setEmail("john@example.com");
        owner.setPassword("password");
        owner.setRole(Role.USER);
        owner = userRepository.save(owner);

        book1 = new Book();
        book1.setTitle("Test Book 1");
        book1.setAuthor("Author 1");
        book1.setIsbn("978-0-123456-78-9");
        book1.setCondition("Good");
        book1.setOwner(owner);
        book1.setStatus(BookStatus.AVAILABLE);
        book1 = bookRepository.save(book1);

        book2 = new Book();
        book2.setTitle("Test Book 2");
        book2.setAuthor("Author 2");
        book2.setIsbn("978-0-987654-32-1");
        book2.setCondition("Excellent");
        book2.setOwner(owner);
        book2.setStatus(BookStatus.RESERVED);
        book2 = bookRepository.save(book2);
    }

    @Test
    void testSearchBooksByTitle() {
        List<Book> books = bookRepository.searchBooks("Test Book 1", null, null, null);
        assertThat(books).hasSize(1);
        assertThat(books.get(0).getTitle()).isEqualTo("Test Book 1");
    }

    @Test
    void testSearchBooksByAuthor() {
        List<Book> books = bookRepository.searchBooks(null, "Author 1", null, null);
        assertThat(books).hasSize(1);
        assertThat(books.get(0).getAuthor()).isEqualTo("Author 1");
    }

    @Test
    void testSearchBooksByStatus() {
        List<Book> books = bookRepository.searchBooks(null, null, BookStatus.AVAILABLE, null);
        assertThat(books).hasSize(1);
        assertThat(books.get(0).getStatus()).isEqualTo(BookStatus.AVAILABLE);
    }

    @Test
    void testSearchBooksByOwnerId() {
        List<Book> books = bookRepository.searchBooks(null, null, null, owner.getId());
        assertThat(books).hasSize(2);
    }

    @Test
    void testFindByOwnerIdAndDeletedFalse() {
        List<Book> books = bookRepository.findByOwnerIdAndDeletedFalse(owner.getId());
        assertThat(books).hasSize(2);
    }

    @Test
    void testPartialSearchByTitle() {
        List<Book> books = bookRepository.searchBooks("Book", null, null, null);
        assertThat(books).hasSize(2);
    }

    @Test
    void testPartialSearchByAuthor() {
        List<Book> books = bookRepository.searchBooks(null, "Author", null, null);
        assertThat(books).hasSize(2);
    }
}

