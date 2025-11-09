package com.example.bookswap.controller;

import com.example.bookswap.dto.SwapRequestDto;
import com.example.bookswap.dto.SwapRequestResponse;
import com.example.bookswap.service.SwapRequestService;
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

@RestController
@RequestMapping("/api/swaps")
@Tag(name = "Swap Requests", description = "Swap request management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class SwapRequestController {
    @Autowired
    private SwapRequestService swapRequestService;

    @PostMapping
    @Operation(summary = "Create a swap request")
    public ResponseEntity<SwapRequestResponse> createSwapRequest(
            @Valid @RequestBody SwapRequestDto request,
            Authentication authentication) {
        String userEmail = authentication.getName();
        SwapRequestResponse response = swapRequestService.createSwapRequest(request, userEmail);
        addHateoasLinks(response);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/accept")
    @Operation(summary = "Accept a swap request (transactional status update)")
    public ResponseEntity<SwapRequestResponse> acceptSwapRequest(
            @PathVariable Long id,
            Authentication authentication) {
        String userEmail = authentication.getName();
        SwapRequestResponse response = swapRequestService.acceptSwapRequest(id, userEmail);
        addHateoasLinks(response);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/reject")
    @Operation(summary = "Reject a swap request (transactional status update)")
    public ResponseEntity<SwapRequestResponse> rejectSwapRequest(
            @PathVariable Long id,
            Authentication authentication) {
        String userEmail = authentication.getName();
        SwapRequestResponse response = swapRequestService.rejectSwapRequest(id, userEmail);
        addHateoasLinks(response);
        return ResponseEntity.ok(response);
    }

    private void addHateoasLinks(SwapRequestResponse swapRequest) {
        Link selfLink = WebMvcLinkBuilder.linkTo(SwapRequestController.class)
                .slash(swapRequest.getId())
                .withSelfRel();
        swapRequest.add(selfLink);

        Link bookLink = WebMvcLinkBuilder.linkTo(BookController.class)
                .slash(swapRequest.getBookId())
                .withRel("book");
        swapRequest.add(bookLink);
    }
}

