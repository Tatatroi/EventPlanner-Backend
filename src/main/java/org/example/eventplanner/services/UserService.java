package org.example.eventplanner.services;

import lombok.Getter;
import org.example.eventplanner.models.User;
import org.example.eventplanner.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Getter
@Service
public class UserService {
    @Getter
    private final UserRepository userRepository;
    private final EventUserService eventUserService;

    public UserService(UserRepository userRepository, EventUserService eventUserService) {
        this.userRepository = userRepository;
        this.eventUserService = eventUserService;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setName(updatedUser.getName());
                    user.setLast_name(updatedUser.getLast_name());
                    user.setEmail(updatedUser.getEmail());
                    user.setPassword(updatedUser.getPassword());
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public void loginUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user == null || !user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid email or password");
        }
    }

    public User getUserByEmail(String email){
        User user = userRepository.findByEmail(email);
        if (user == null){
            throw new RuntimeException("User does not exist");
        }

        return user;
    }

}