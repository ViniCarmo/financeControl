package com.vinicius.finance_api.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record UserRequestDto(
                             @NotNull(message = "Username cannot be null")
                             @JsonProperty("username")
                             String username,

                             @NotNull(message = "Email cannot be null")
                             @JsonProperty("email")
                             String email,

                             @NotNull(message = "Password cannot be null")
                             @JsonProperty("password")
                             String password) {
}
