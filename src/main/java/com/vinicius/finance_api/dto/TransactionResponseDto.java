package com.vinicius.finance_api.dto;

import com.vinicius.finance_api.enums.TransactionType;

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
