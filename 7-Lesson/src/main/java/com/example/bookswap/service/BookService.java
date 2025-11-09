package com.example.bookswap.service;

import com.example.bookswap.dto.BookRequest;
import com.example.bookswap.dto.BookResponse;
import com.example.bookswap.dto.BookStatusUpdateRequest;
import com.example.bookswap.model.Book;
import com.example.bookswap.model.BookStatus;
import com.example.bookswap.model.User;
import com.example.bookswap.repository.BookRepository;
import com.example.bookswap.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    public List<BookResponse> searchBooks(String title, String author, BookStatus status, Long ownerId) {
        List<Book> books = bookRepository.searchBooks(title, author, status, ownerId);
        return books.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public BookResponse createBook(BookRequest request, String userEmail) {
        User owner = userRepository.findByEmailAndDeletedFalse(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if ISBN already exists (normalize ISBN for comparison)
        String normalizedIsbn = request.getIsbn().replaceAll("[^0-9X]", "");
        bookRepository.findByDeletedFalse().stream()
                .filter(b -> b.getIsbn().replaceAll("[^0-9X]", "").equals(normalizedIsbn))
                .findFirst()
                .ifPresent(b -> {
                    throw new RuntimeException("Book with this ISBN already exists");
                });

        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setCondition(request.getCondition());
        book.setOwner(owner);
        book.setStatus(BookStatus.AVAILABLE);

        book = bookRepository.save(book);
        return convertToResponse(book);
    }

    @Transactional
    public BookResponse updateBookStatus(Long id, BookStatusUpdateRequest request, String userEmail) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (book.isDeleted()) {
            throw new RuntimeException("Book not found");
        }

        User user = userRepository.findByEmailAndDeletedFalse(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Only owner or admin can update status
        if (!book.getOwner().getId().equals(user.getId()) && !user.getRole().name().equals("ADMIN")) {
            throw new RuntimeException("Unauthorized to update this book");
        }

        // Validate status transitions
        validateStatusTransition(book.getStatus(), request.getStatus());

        book.setStatus(request.getStatus());
        book = bookRepository.save(book);
        return convertToResponse(book);
    }

    public List<BookResponse> getBooksByOwner(Long ownerId) {
        List<Book> books = bookRepository.findByOwnerIdAndDeletedFalse(ownerId);
        return books.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private void validateStatusTransition(BookStatus currentStatus, BookStatus newStatus) {
        // Define valid transitions
        if (currentStatus == BookStatus.SWAPPED && newStatus != BookStatus.SWAPPED) {
            throw new RuntimeException("Cannot change status from SWAPPED");
        }
        if (currentStatus == BookStatus.AVAILABLE && newStatus == BookStatus.SWAPPED) {
            throw new RuntimeException("Cannot directly change from AVAILABLE to SWAPPED");
        }
    }

    private BookResponse convertToResponse(Book book) {
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getCondition(),
                book.getOwner().getId(),
                book.getOwner().getName(),
                book.getStatus(),
                book.getCreatedAt()
        );
    }
}

