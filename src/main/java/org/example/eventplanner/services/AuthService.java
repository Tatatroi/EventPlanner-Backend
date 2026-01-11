package org.example.eventplanner.services;

import org.example.eventplanner.dto.AuthResponse;
import org.example.eventplanner.dto.LogInRequest;
import org.example.eventplanner.dto.RegisterRequest;
import org.example.eventplanner.models.User;
import org.example.eventplanner.repositories.UserRepository;
import org.example.eventplanner.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse register(RegisterRequest registerRequest) {
        User existingUser = userRepository.findByEmail(registerRequest.getEmail());

        User userToSave;

        if (existingUser != null) {
            if ("Guest".equalsIgnoreCase(existingUser.getRole())) {
                userToSave = existingUser;
                userToSave.setName(registerRequest.getName());
                userToSave.setLast_name(registerRequest.getLastName());

                userToSave.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

                userToSave.setRole("User");

            } else {
                throw new IllegalArgumentException("Email already registered");
            }

        } else {
            userToSave = new User();
            userToSave.setEmail(registerRequest.getEmail());
            userToSave.setName(registerRequest.getName());
            userToSave.setLast_name(registerRequest.getLastName());
            userToSave.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            userToSave.setRole("User");
        }

        User saved = userRepository.save(userToSave);

        String token = jwtUtil.generateJwtToken(saved);

        return new AuthResponse(
                token,
                saved.getIdUser(),
                saved.getEmail()
        );
    }

    public AuthResponse login(LogInRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail());

        if (user == null) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        String token = jwtUtil.generateJwtToken(user);

        return new AuthResponse(
                token,
                user.getIdUser(),
                user.getEmail());
    }
}