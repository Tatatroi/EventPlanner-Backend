package org.example.eventplanner.services;

import org.example.eventplanner.dto.AuthResponse;
import org.example.eventplanner.dto.LogInRequest;
import org.example.eventplanner.dto.RegisterRequest;
import org.example.eventplanner.models.User;
import org.example.eventplanner.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        User user = new User();
        user.setName(registerRequest.getName());
        user.setLast_name(registerRequest.getLastName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        User saved = userRepository.save(user);

        return new AuthResponse(saved.getId_user(), saved.getEmail(), "Registered successfully");

    }

    public AuthResponse login(LogInRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail());
//                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        return new AuthResponse(user.getId_user(), user.getEmail(), "Login successful");
    }
}
