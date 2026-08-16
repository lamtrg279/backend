package com.printledger.backend.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.printledger.backend.entity.User;
import com.printledger.backend.repository.UserRepository;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Injects the user repository and password encoder used by this service.
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Validates, hashes, and saves a new user.
    public User createUser(User user) {
        validateRequiredFields(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // Finds a user by ID or throws an error when the user does not exist.
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    // Retrieves all users from the database.
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Updates a user's email and status without changing the stored password hash.
    public User updateUser(Long id, User userDetails) {
        User existingUser = getUserById(id);

        validateRequiredFields(userDetails);

        existingUser.setEmail(userDetails.getEmail());
        existingUser.setStatus(userDetails.getStatus());

        return userRepository.save(existingUser);
    }

    // Deletes a user by ID.
    public void deleteUser(Long id) {
        User existingUser = getUserById(id);
        userRepository.delete(existingUser);
    }

    // Checks that the user's required fields are present and unique.
    public void validateRequiredFields(User user) {
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new IllegalArgumentException("User name is required.");
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new IllegalArgumentException("User email is required.");
        }
        if (user.getUsername() != null && userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("User name already exists.");
        }
        if (user.getEmail() != null && userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("User email already exists.");
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("User password is required.");
        }

    }

}
