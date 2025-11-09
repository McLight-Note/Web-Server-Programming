package com.example.bookswap.repository;

import com.example.bookswap.model.SwapRequest;
import com.example.bookswap.model.SwapRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SwapRequestRepository extends JpaRepository<SwapRequest, Long> {
    List<SwapRequest> findByDeletedFalse();
    Optional<SwapRequest> findByIdAndDeletedFalse(Long id);
    List<SwapRequest> findByBookIdAndDeletedFalse(Long bookId);
    List<SwapRequest> findByRequesterIdAndDeletedFalse(Long requesterId);
    List<SwapRequest> findByStatusAndDeletedFalse(SwapRequestStatus status);
}

