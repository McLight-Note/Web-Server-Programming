package com.example.registrationservice.controller;

import com.example.registrationservice.dto.RegistrationResponse;
import com.example.registrationservice.dto.UserRegistrationRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class RegistrationController {
    
    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> registerUser(@Valid @RequestBody UserRegistrationRequest request) {
        try {
            String userId = UUID.randomUUID().toString();
            
            String message = String.format("User '%s' with email '%s' has been successfully registered!", 
                                         request.getName(), request.getEmail());
            
            RegistrationResponse response = new RegistrationResponse(
                message,
                "SUCCESS",
                userId
            );
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            RegistrationResponse errorResponse = new RegistrationResponse(
                "Registration failed due to an internal error: " + e.getMessage(),
                "ERROR",
                null
            );
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Registration service is running");
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RegistrationResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errorMessage = new StringBuilder("Validation failed: ");
        
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errorMessage.append(error.getField()).append(" - ").append(error.getDefaultMessage()).append("; ");
        });
        
        RegistrationResponse errorResponse = new RegistrationResponse(
            errorMessage.toString(),
            "VALIDATION_ERROR",
            null
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
