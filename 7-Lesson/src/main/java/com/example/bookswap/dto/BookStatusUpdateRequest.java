package com.example.bookswap.dto;

import com.example.bookswap.model.BookStatus;
import jakarta.validation.constraints.NotNull;

public class BookStatusUpdateRequest {
    @NotNull(message = "Status is required")
    private BookStatus status;

    public BookStatusUpdateRequest() {}

    public BookStatusUpdateRequest(BookStatus status) {
        this.status = status;
    }

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }
}

