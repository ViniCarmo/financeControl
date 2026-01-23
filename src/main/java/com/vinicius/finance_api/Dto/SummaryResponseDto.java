package com.vinicius.finance_api.Dto;

public record SummaryResponseDto(
        Double totalIncome,
        Double totalExpensive,
        Double balance,
        Double totalTransactions) {
}
