package com.vinicius.finance_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vinicius.finance_api.enums.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record TransactionRequestDto(
        @NotNull(message = "Value cannot be null")
        @Positive(message = "Value must be greater than zero")
        @JsonProperty("value")
        Double value,

        @NotNull(message = "Type cannot be null")
        @JsonProperty("type")
        TransactionType type,

        @NotNull(message = "Date canno1" + " be null")
        @JsonProperty("date")
        LocalDate date,

        @NotBlank(message = "Description cannot be blank")
        @Size(max = 255, message = "Description must have at most 255 characters")
        @JsonProperty("description")
        String description,

        @NotNull(message = "User ID cannot be null")
        @Positive(message = "User ID must be a positive number")
        @JsonProperty("userId")
        Integer userId
) {

}
