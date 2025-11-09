package com.example.bookswap.dto;

import com.example.bookswap.model.SwapRequestStatus;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

public class SwapRequestResponse extends RepresentationModel<SwapRequestResponse> {
    private Long id;
    private Long bookId;
    private String bookTitle;
    private Long requesterId;
    private String requesterName;
    private String message;
    private SwapRequestStatus status;
    private LocalDateTime createdAt;

    public SwapRequestResponse() {}

    public SwapRequestResponse(Long id, Long bookId, String bookTitle, Long requesterId,
                              String requesterName, String message, SwapRequestStatus status,
                              LocalDateTime createdAt) {
        this.id = id;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.requesterId = requesterId;
        this.requesterName = requesterName;
        this.message = message;
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

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public Long getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(Long requesterId) {
        this.requesterId = requesterId;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public SwapRequestStatus getStatus() {
        return status;
    }

    public void setStatus(SwapRequestStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

