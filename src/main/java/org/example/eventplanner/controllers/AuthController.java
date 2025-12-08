package org.example.eventplanner.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.eventplanner.dto.AuthResponse;
import org.example.eventplanner.dto.LogInRequest;
import org.example.eventplanner.dto.RegisterRequest;
import org.example.eventplanner.security.JwtUtil;
import org.example.eventplanner.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins="http://localhost:3000")
public class AuthController {
    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest req) {
        return ResponseEntity.ok(authService.register(req));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LogInRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }

}