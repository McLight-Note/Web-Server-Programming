package com.example.bookswap.dto;

import jakarta.validation.constraints.NotNull;

public class SwapRequestDto {
    @NotNull(message = "Book ID is required")
    private Long bookId;

    private String message;

    public SwapRequestDto() {}

    public SwapRequestDto(Long bookId, String message) {
        this.bookId = bookId;
        this.message = message;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

