package com.example.bookswap.dto;

import com.example.bookswap.model.BookStatus;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

public class BookResponse extends RepresentationModel<BookResponse> {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private String condition;
    private Long ownerId;
    private String ownerName;
    private BookStatus status;
    private LocalDateTime createdAt;

    public BookResponse() {}

    public BookResponse(Long id, String title, String author, String isbn, String condition,
                       Long ownerId, String ownerName, BookStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.condition = condition;
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

