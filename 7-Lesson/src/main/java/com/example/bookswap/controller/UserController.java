package com.example.bookswap.controller;

import com.example.bookswap.dto.BookResponse;
import com.example.bookswap.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "User endpoints")
@SecurityRequirement(name = "bearerAuth")
public class UserController {
    @Autowired
    private BookService bookService;

    @GetMapping("/{id}/books")
    @Operation(summary = "Get all books by owner")
    public ResponseEntity<List<BookResponse>> getUserBooks(@PathVariable Long id) {
        List<BookResponse> books = bookService.getBooksByOwner(id);
        books.forEach(book -> {
            Link selfLink = WebMvcLinkBuilder.linkTo(BookController.class)
                    .slash(book.getId())
                    .withSelfRel();
            book.add(selfLink);
        });
        return ResponseEntity.ok(books);
    }
}

