package com.vinicius.finance_api.dto;

import java.time.LocalDate;

public record SummaryResponseDto(
        Integer id,
        Double totalIncome,
        Double totalExpense,
        Double balance,
        Integer totalTransactions,
        LocalDate initialDate,
        LocalDate finalDate) {
}
