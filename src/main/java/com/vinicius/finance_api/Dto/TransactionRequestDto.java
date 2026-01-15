package com.vinicius.finance_api.Dto;

import com.vinicius.finance_api.Entities.TransactionType;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record TransactionRequestDto(
        @NotBlank(message = "Value cannot be blank")
        Double value,

        @NotBlank(message = "Type cannot be blank")
        TransactionType type,

        @NotBlank(message = "Date cannot be blank")
        LocalDate date,

        @NotBlank(message = "Description cannot be blank")
        String description
) {

}
