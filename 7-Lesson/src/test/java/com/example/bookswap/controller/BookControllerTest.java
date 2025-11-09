package com.example.bookswap.controller;

import com.example.bookswap.dto.BookRequest;
import com.example.bookswap.dto.BookResponse;
import com.example.bookswap.model.BookStatus;
import com.example.bookswap.model.Role;
import com.example.bookswap.model.User;
import com.example.bookswap.repository.UserRepository;
import com.example.bookswap.security.JwtUtil;
import com.example.bookswap.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private BookResponse bookResponse;

    @BeforeEach
    void setUp() {
        bookResponse = new BookResponse();
        bookResponse.setId(1L);
        bookResponse.setTitle("Test Book");
        bookResponse.setAuthor("Test Author");
        bookResponse.setIsbn("978-0-123456-78-9");
        bookResponse.setCondition("Good");
        bookResponse.setOwnerId(1L);
        bookResponse.setOwnerName("John Doe");
        bookResponse.setStatus(BookStatus.AVAILABLE);
    }

    @Test
    @WithMockUser
    void testSearchBooks() throws Exception {
        List<BookResponse> books = Arrays.asList(bookResponse);
        when(bookService.searchBooks(anyString(), anyString(), any(), anyLong()))
                .thenReturn(books);

        mockMvc.perform(get("/api/books")
                        .param("title", "Test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Book"));
    }

    @Test
    @WithMockUser
    void testCreateBook() throws Exception {
        BookRequest request = new BookRequest();
        request.setTitle("New Book");
        request.setAuthor("New Author");
        request.setIsbn("978-0-987654-32-1");
        request.setCondition("Excellent");

        when(bookService.createBook(any(BookRequest.class), anyString()))
                .thenReturn(bookResponse);

        mockMvc.perform(post("/api/books")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Book"));
    }

    @Test
    @WithMockUser
    void testUpdateBookStatus() throws Exception {
        when(bookService.updateBookStatus(anyLong(), any(), anyString()))
                .thenReturn(bookResponse);

        String requestBody = "{\"status\":\"RESERVED\"}";

        mockMvc.perform(patch("/api/books/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }
}

