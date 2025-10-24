package com.example.registrationservice.dto;

public class RegistrationResponse {
    
    private String message;
    private String status;
    private String userId;
    
    public RegistrationResponse() {}
    
    public RegistrationResponse(String message, String status, String userId) {
        this.message = message;
        this.status = status;
        this.userId = userId;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    @Override
    public String toString() {
        return "RegistrationResponse{" +
                "message='" + message + '\'' +
                ", status='" + status + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
