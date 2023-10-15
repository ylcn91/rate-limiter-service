package com.doksanbir.ratelimiter.dto;

import lombok.Data;

import java.util.Set;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String password;  // Consider not exposing the password field based on the use-case
    private Set<String> roles; // Role names as Strings for simplicity
}

