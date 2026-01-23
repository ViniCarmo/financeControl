package com.vinicius.finance_api.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vinicius.finance_api.Entities.TransactionType;

import java.time.LocalDate;

public record TransactionResponseDto(
        Integer id,
        Double value,
        TransactionType type,
        LocalDate date,
        String description
) {
}
