package com.vinicius.finance_api.Dto;

public record MonthlySummaryResponseDto(
        Double totalIncome,
        Double totalExpensive,
        Double balance,
        Double totalTransactions) {
}
