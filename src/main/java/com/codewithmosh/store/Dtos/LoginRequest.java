package com.codewithmosh.store.Dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "email should not br blank")
    @Email
    private String email;

    @NotBlank(message = "password should not be blank")
    private String password;
}