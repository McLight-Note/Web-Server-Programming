package com.example.bookswap.service;

import com.example.bookswap.dto.SwapRequestDto;
import com.example.bookswap.dto.SwapRequestResponse;
import com.example.bookswap.model.Book;
import com.example.bookswap.model.BookStatus;
import com.example.bookswap.model.SwapRequest;
import com.example.bookswap.model.SwapRequestStatus;
import com.example.bookswap.model.User;
import com.example.bookswap.repository.BookRepository;
import com.example.bookswap.repository.SwapRequestRepository;
import com.example.bookswap.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SwapRequestService {
    @Autowired
    private SwapRequestRepository swapRequestRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public SwapRequestResponse createSwapRequest(SwapRequestDto request, String userEmail) {
        User requester = userRepository.findByEmailAndDeletedFalse(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (book.isDeleted()) {
            throw new RuntimeException("Book not found");
        }

        if (book.getStatus() != BookStatus.AVAILABLE) {
            throw new RuntimeException("Book is not available for swapping");
        }

        if (book.getOwner().getId().equals(requester.getId())) {
            throw new RuntimeException("Cannot request swap for your own book");
        }

        SwapRequest swapRequest = new SwapRequest();
        swapRequest.setBook(book);
        swapRequest.setRequester(requester);
        swapRequest.setMessage(request.getMessage());
        swapRequest.setStatus(SwapRequestStatus.PENDING);

        swapRequest = swapRequestRepository.save(swapRequest);
        return convertToResponse(swapRequest);
    }

    @Transactional
    public SwapRequestResponse acceptSwapRequest(Long id, String userEmail) {
        SwapRequest swapRequest = swapRequestRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Swap request not found"));

        User user = userRepository.findByEmailAndDeletedFalse(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Book book = swapRequest.getBook();

        // Only book owner can accept
        if (!book.getOwner().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized to accept this swap request");
        }

        if (swapRequest.getStatus() != SwapRequestStatus.PENDING) {
            throw new RuntimeException("Swap request is not in PENDING status");
        }

        if (book.getStatus() != BookStatus.AVAILABLE) {
            throw new RuntimeException("Book is no longer available");
        }

        // Transactional update: mark request as accepted and book as swapped
        swapRequest.setStatus(SwapRequestStatus.ACCEPTED);
        book.setStatus(BookStatus.SWAPPED);
        
        // Reject other pending requests for the same book
        List<SwapRequest> otherRequests = swapRequestRepository.findByBookIdAndDeletedFalse(book.getId());
        otherRequests.stream()
                .filter(req -> req.getStatus() == SwapRequestStatus.PENDING && !req.getId().equals(id))
                .forEach(req -> req.setStatus(SwapRequestStatus.REJECTED));

        swapRequestRepository.save(swapRequest);
        bookRepository.save(book);
        swapRequestRepository.saveAll(otherRequests);

        return convertToResponse(swapRequest);
    }

    @Transactional
    public SwapRequestResponse rejectSwapRequest(Long id, String userEmail) {
        SwapRequest swapRequest = swapRequestRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Swap request not found"));

        User user = userRepository.findByEmailAndDeletedFalse(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Book book = swapRequest.getBook();

        // Only book owner can reject
        if (!book.getOwner().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized to reject this swap request");
        }

        if (swapRequest.getStatus() != SwapRequestStatus.PENDING) {
            throw new RuntimeException("Swap request is not in PENDING status");
        }

        swapRequest.setStatus(SwapRequestStatus.REJECTED);
        swapRequest = swapRequestRepository.save(swapRequest);
        return convertToResponse(swapRequest);
    }

    private SwapRequestResponse convertToResponse(SwapRequest swapRequest) {
        return new SwapRequestResponse(
                swapRequest.getId(),
                swapRequest.getBook().getId(),
                swapRequest.getBook().getTitle(),
                swapRequest.getRequester().getId(),
                swapRequest.getRequester().getName(),
                swapRequest.getMessage(),
                swapRequest.getStatus(),
                swapRequest.getCreatedAt()
        );
    }
}

