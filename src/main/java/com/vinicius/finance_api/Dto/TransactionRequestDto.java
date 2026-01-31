package com.vinicius.finance_api.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vinicius.finance_api.Entities.User;
import com.vinicius.finance_api.Enums.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record TransactionRequestDto(
        @NotNull(message = "Value cannot be null")
        @JsonProperty("value")
        Double value,

        @NotNull(message = "Type cannot be null")
        @JsonProperty("type")
        TransactionType type,

        @NotNull(message = "Date canno1" + " be null")
        @JsonProperty("date")
        LocalDate date,

        @NotBlank(message = "Description cannot be blank")
        @JsonProperty("description")
        String description,

        @NotNull(message = "User ID cannot be null")
        @JsonProperty("userId")
        Integer userId
) {

}
