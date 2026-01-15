package com.vinicius.finance_api.Dto;

public record TransactionResponseDto(
        Integer id,
        Double value,
        String type,
        String date,
        String description
) {
}
