package org.example.eventplanner.dto;

public class AuthResponse {
    private Long userId;
    private String email;
    private String message;
    // dacă mai târziu pui JWT, adaugi aici token


    public AuthResponse(Long userId, String email, String message) {
        this.userId = userId;
        this.email = email;
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getMessage() {
        return message;
    }
}
