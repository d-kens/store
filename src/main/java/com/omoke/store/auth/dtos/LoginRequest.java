package com.omoke.store.auth.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid email")
    private String email;

    @NotBlank(message = "password is required")
    private String password;
}
