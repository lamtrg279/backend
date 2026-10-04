package com.printledger.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.printledger.backend.dto.LoginResponse;
import com.printledger.backend.entity.User;
import com.printledger.backend.entity.UserRole;
import com.printledger.backend.entity.UserStatus;
import com.printledger.backend.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User activeUser;

    @BeforeEach
    void setUp() {
        activeUser = User.builder()
                .id(1L)
                .username("lam")
                .email("lam@example.com")
                .password("hashedSecret123")
                .role(UserRole.USER)
                .status(UserStatus.ACTIVE)
                .build();
    }

    @Test
    void createUserHashesPasswordAndSetsDefaultRoleAndStatus() {
        User newUser = User.builder()
                .username("lam")
                .email("lam@example.com")
                .password("secret123")
                .build();

        when(userRepository.existsByUsername("lam")).thenReturn(false);
        when(userRepository.existsByEmail("lam@example.com")).thenReturn(false);
        when(passwordEncoder.encode("secret123")).thenReturn("hashedSecret123");
        when(userRepository.save(newUser)).thenReturn(newUser);

        User result = userService.createUser(newUser);

        assertEquals("hashedSecret123", result.getPassword());
        assertEquals(UserRole.USER, result.getRole());
        assertEquals(UserStatus.ACTIVE, result.getStatus());
        verify(userRepository).save(newUser);
    }

    @Test
    void loginReturnsUserDetailsForValidCredentials() {
        when(userRepository.findByUsername(activeUser.getUsername())).thenReturn(activeUser);
        when(passwordEncoder.matches("secret123", activeUser.getPassword())).thenReturn(true);

        LoginResponse result = userService.login(activeUser.getUsername(), "secret123");

        assertEquals(activeUser.getId(), result.getId());
        assertEquals(activeUser.getUsername(), result.getUsername());
        assertEquals(activeUser.getEmail(), result.getEmail());
        assertEquals(activeUser.getRole(), result.getRole());
        assertEquals(activeUser.getStatus(), result.getStatus());
    }

    @Test
    void loginRejectUnknownUsername() {
        when(userRepository.findByUsername("unknown")).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> userService.login("unknown", "password"));

        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    void loginRejectInactiveUser() {
        activeUser.setStatus(UserStatus.INACTIVE);

        when(userRepository.findByUsername(activeUser.getUsername())).thenReturn(activeUser);
        when(passwordEncoder.matches("secret123", activeUser.getPassword())).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> userService.login(activeUser.getUsername(), "secret123"));
    }

    @Test
    void loginRejectIncorrectPassword() {
        when(userRepository.findByUsername(activeUser.getUsername())).thenReturn(activeUser);
        when(passwordEncoder.matches("incorrectPassword", activeUser.getPassword())).thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> userService.login(activeUser.getUsername(), "incorrectPassword"));
    }

}
