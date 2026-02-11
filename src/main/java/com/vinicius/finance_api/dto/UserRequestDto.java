package com.vinicius.finance_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRequestDto(
        @NotBlank(message = "Username cannot be blank")
        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
        @JsonProperty("username")
        String username,

        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Email must be valid")
        @JsonProperty("email")
        String email,

        @NotBlank(message = "Password cannot be blank")
        @Size(min = 6, max = 100, message = "Password must have at least 6 characters")
        @JsonProperty("password")
        String password) {
}
