package com.vinicius.finance_api.Dto;

import java.time.LocalDate;

public record MonthlySummaryResponseDto(
        Integer id,
        Double totalIncome,
        Double totalExpense,
        Double balance,
        Integer totalTransactions,
        LocalDate initialDate,
        LocalDate finalDate) {
}
