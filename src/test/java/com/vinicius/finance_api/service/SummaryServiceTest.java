package com.vinicius.finance_api.service;

import com.vinicius.finance_api.entity.Summary;
import com.vinicius.finance_api.entity.Transaction;
import com.vinicius.finance_api.entity.User;
import com.vinicius.finance_api.enums.TransactionType;
import com.vinicius.finance_api.repositories.SummaryRepository;
import com.vinicius.finance_api.repositories.TransactionRepository;
import com.vinicius.finance_api.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SummaryServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private SummaryRepository summaryRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SummaryService summaryService;

    @Test
    @DisplayName("Should generate summary successfully")
    void gerarSummary_ShouldSave_WhenUserExists() {
        Integer userId = 1;
        LocalDate start = LocalDate.of(2026, 3, 1);
        LocalDate end = LocalDate.of(2026, 3, 31);
        User user = new User(userId, "Vinicius", "vinicius@gmail.com", "senha", null);

        Transaction income = new Transaction(1, 1000.0, TransactionType.INCOME, start, "Salário", user);
        Transaction expense = new Transaction(2, 500.0, TransactionType.EXPENSE, start, "Aluguel", user);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(transactionRepository.findByUserIdAndDateBetween(userId, start, end))
                .thenReturn(List.of(income, expense));
        when(summaryRepository.save(any(Summary.class))).thenAnswer(i -> i.getArgument(0));

        Summary result = summaryService.gerarSummary(userId, start, end);

        verify(summaryRepository, times(1)).save(any(Summary.class));
        assertThat(result.getTotalIncome()).isEqualTo(1000.0);
        assertThat(result.getTotalExpense()).isEqualTo(500.0);
        assertThat(result.getBalance()).isEqualTo(500.0);
        assertThat(result.getTotalTransactions()).isEqualTo(2);
    }

    @Test
    @DisplayName("Should throw exception when user not found")
    void gerarSummary_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> summaryService.gerarSummary(99, LocalDate.now(), LocalDate.now()));

        verify(summaryRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should calculate total income correctly")
    void totalIncome_ShouldReturnCorrectValue() {
        User user = new User(1, "Vinicius", "vinicius@gmail.com", "senha", null);
        List<Transaction> transactions = List.of(
                new Transaction(1, 1000.0, TransactionType.INCOME, LocalDate.now(), "Salário", user),
                new Transaction(2, 500.0, TransactionType.INCOME, LocalDate.now(), "Freelance", user),
                new Transaction(3, 200.0, TransactionType.EXPENSE, LocalDate.now(), "Aluguel", user)
        );

        Double result = summaryService.totalIncome(transactions);

        assertThat(result).isEqualTo(1500.0);
    }

    @Test
    @DisplayName("Should calculate total expense correctly")
    void totalExpense_ShouldReturnCorrectValue() {
        User user = new User(1, "Vinicius", "vinicius@gmail.com", "senha", null);
        List<Transaction> transactions = List.of(
                new Transaction(1, 1000.0, TransactionType.INCOME, LocalDate.now(), "Salário", user),
                new Transaction(2, 300.0, TransactionType.EXPENSE, LocalDate.now(), "Aluguel", user),
                new Transaction(3, 200.0, TransactionType.EXPENSE, LocalDate.now(), "Mercado", user)
        );

        Double result = summaryService.totalExpense(transactions);

        assertThat(result).isEqualTo(500.0);
    }

    @Test
    @DisplayName("Should calculate balance correctly")
    void balance_ShouldReturnCorrectValue() {
        Double totalIncome = 1000.0;
        Double totalExpense = 600.0;

        Double result = summaryService.balance(totalIncome, totalExpense);

        assertThat(result).isEqualTo(400.0);
    }

    @Test
    @DisplayName("Should return negative balance when expense is greater than income")
    void balance_ShouldReturnNegativeValue_WhenExpenseIsGreater() {
        Double totalIncome = 500.0;
        Double totalExpense = 800.0;

        Double result = summaryService.balance(totalIncome, totalExpense);

        assertThat(result).isEqualTo(-300.0);
    }
}