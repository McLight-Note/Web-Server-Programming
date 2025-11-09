package com.example.bookswap.controller;

import com.example.bookswap.dto.BookRequest;
import com.example.bookswap.dto.BookResponse;
import com.example.bookswap.dto.BookStatusUpdateRequest;
import com.example.bookswap.model.BookStatus;
import com.example.bookswap.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@Tag(name = "Books", description = "Book management endpoints")
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping
    @Operation(summary = "Search books with filters")
    public ResponseEntity<List<BookResponse>> searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) BookStatus status,
            @RequestParam(required = false) Long ownerId) {
        List<BookResponse> books = bookService.searchBooks(title, author, status, ownerId);
        books.forEach(this::addHateoasLinks);
        return ResponseEntity.ok(books);
    }

    @PostMapping
    @Operation(summary = "Create a new book (only owner can create)")
    @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<BookResponse> createBook(
            @Valid @RequestBody BookRequest request,
            Authentication authentication) {
        String userEmail = authentication.getName();
        BookResponse response = bookService.createBook(request, userEmail);
        addHateoasLinks(response);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update book status")
    @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<BookResponse> updateBookStatus(
            @PathVariable Long id,
            @Valid @RequestBody BookStatusUpdateRequest request,
            Authentication authentication) {
        String userEmail = authentication.getName();
        BookResponse response = bookService.updateBookStatus(id, request, userEmail);
        addHateoasLinks(response);
        return ResponseEntity.ok(response);
    }

    private void addHateoasLinks(BookResponse book) {
        Link selfLink = WebMvcLinkBuilder.linkTo(BookController.class)
                .slash(book.getId())
                .withSelfRel();
        book.add(selfLink);

        Link ownerLink = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(UserController.class)
                        .getUserBooks(book.getOwnerId()))
                .withRel("owner");
        book.add(ownerLink);

        Link swapLink = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(SwapRequestController.class)
                        .createSwapRequest(null, null))
                .withRel("swap");
        book.add(swapLink);
    }
}

