package com.vinicius.finance_api.Dto;

import jakarta.validation.constraints.NotBlank;

public record TransactionRequestDto(
        @NotBlank(message = "Value cannot be blank")
        Double value,

        @NotBlank(message = "Type cannot be blank")
        String type,

        @NotBlank(message = "Date cannot be blank")
        String date,

        @NotBlank(message = "Description cannot be blank")
        String description
) {

}
