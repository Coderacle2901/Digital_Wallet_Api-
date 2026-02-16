package com.example.Digital_Wallet_Api.user.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserRequestDTO {

    @NotBlank(message = "FirstName field cannot be empty")
    private String firstName;

    @NotBlank(message = "LastName field cannot be empty")
    private String lastName;

    @NotBlank(message = "email is required")
    @Email(message = "Please enter a valid email address, example: example@gmail.com")
    private String email;

    @NotBlank(message = "password is required")
   @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,}$",
           message = "Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one number, and one special character")
    private String password;
}
