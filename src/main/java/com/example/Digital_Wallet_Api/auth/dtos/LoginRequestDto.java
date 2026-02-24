package com.example.Digital_Wallet_Api.auth.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequestDto {
    @NotBlank(message = "Email field cannot be empty")
    @Email(message = "Enter a valid email address, example: example@gmail.com")
    private String email;

    @NotBlank(message = "Password field cannot be empty")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
}
