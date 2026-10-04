package com.printledger.backend.dto;

import com.printledger.backend.entity.UserRole;
import com.printledger.backend.entity.UserStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private Long id;
    private String username;
    private String email;
    private UserRole role;
    private UserStatus status;
}
