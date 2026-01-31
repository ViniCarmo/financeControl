package com.vinicius.finance_api.Dto;

import com.vinicius.finance_api.Enums.TransactionType;

import java.time.LocalDate;

public record TransactionResponseDto(
        Integer id,
        Double value,
        TransactionType type,
        LocalDate date,
        String description,
        Integer userId
) {
}
