package com.printledger.backend.service;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.printledger.backend.entity.User;
import com.printledger.backend.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = userRepository.findByUsername(username);
        if (u == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        String role = (u.getRole() != null) ? u.getRole().name() : "USER";
        return org.springframework.security.core.userdetails.User
                .withUsername(u.getUsername())
                .password(u.getPassword())
                .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role)))
                .accountExpired(false)
                .credentialsExpired(false)
                .build();
    }

}
