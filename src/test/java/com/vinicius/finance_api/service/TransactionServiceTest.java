package com.vinicius.finance_api.service;

import com.vinicius.finance_api.dto.TransactionRequestDto;
import com.vinicius.finance_api.dto.TransactionResponseDto;
import com.vinicius.finance_api.entity.Transaction;
import com.vinicius.finance_api.entity.User;
import com.vinicius.finance_api.enums.TransactionType;
import com.vinicius.finance_api.repositories.TransactionRepository;
import com.vinicius.finance_api.repositories.UserRepository;
import com.vinicius.finance_api.service.TransactionService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    @DisplayName("Should save transaction successfully")
    void saveTransaction_ShouldSave_WhenUserExists() {
        Integer userId = 1;
        User user = new User(userId, "Vinicius", "vinicius@gmail.com", "senha", null);
        TransactionRequestDto dto = new TransactionRequestDto(
                100.0, TransactionType.INCOME, LocalDate.now(), "Salário");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        transactionService.saveTransaction(dto, userId);

        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    @DisplayName("Should throw exception when user not found on save")
    void saveTransaction_ShouldThrowException_WhenUserNotFound() {
        Integer userId = 99;
        TransactionRequestDto dto = new TransactionRequestDto(
                100.0, TransactionType.INCOME, LocalDate.now(), "Salário");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> transactionService.saveTransaction(dto, userId));

        verify(transactionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should return transaction when found")
    void getTransactionById_ShouldReturnTransaction_WhenFound() {
        Integer id = 1;
        Integer userId = 1;
        User user = new User(userId, "Vinicius", "vinicius@gmail.com", "senha", null);
        Transaction transaction = new Transaction(id, 100.0, TransactionType.INCOME,
                LocalDate.now(), "Salário", user);

        when(transactionRepository.findByIdAndUserId(id, userId))
                .thenReturn(Optional.of(transaction));

        TransactionResponseDto result = transactionService.getTransactionById(id, userId);

        assertThat(result.id()).isEqualTo(id);
        assertThat(result.value()).isEqualTo(100.0);
        assertThat(result.type()).isEqualTo(TransactionType.INCOME);
    }

    @Test
    @DisplayName("Should throw exception when transaction not found")
    void getTransactionById_ShouldThrowException_WhenNotFound() {
        when(transactionRepository.findByIdAndUserId(any(), any()))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> transactionService.getTransactionById(1, 1));
    }

    @Test
    @DisplayName("Should delete transaction successfully")
    void deleteTransactionById_ShouldDelete_WhenFound() {
        Integer id = 1;
        Integer userId = 1;
        User user = new User(userId, "Vinicius", "vinicius@gmail.com", "senha", null);
        Transaction transaction = new Transaction(id, 100.0, TransactionType.INCOME,
                LocalDate.now(), "Salário", user);

        when(transactionRepository.findByIdAndUserId(id, userId))
                .thenReturn(Optional.of(transaction));

        transactionService.deleteTransactionById(id, userId);

        verify(transactionRepository, times(1)).delete(transaction);
    }

    @Test
    @DisplayName("Should throw exception when transaction not found on delete")
    void deleteTransactionById_ShouldThrowException_WhenNotFound() {
        when(transactionRepository.findByIdAndUserId(any(), any()))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> transactionService.deleteTransactionById(1, 1));

        verify(transactionRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Should update transaction successfully")
    void updateTransactionById_ShouldUpdate_WhenFound() {
        Integer id = 1;
        Integer userId = 1;
        User user = new User(userId, "Vinicius", "vinicius@gmail.com", "senha", null);
        Transaction existing = new Transaction(id, 100.0, TransactionType.INCOME,
                LocalDate.now(), "Salário", user);
        TransactionRequestDto dto = new TransactionRequestDto(
                200.0, TransactionType.EXPENSE, LocalDate.now(), "Aluguel");

        when(transactionRepository.findByIdAndUserId(id, userId))
                .thenReturn(Optional.of(existing));

        transactionService.updateTransactionById(id, dto, userId);

        verify(transactionRepository, times(1)).save(existing);
        assertThat(existing.getValue()).isEqualTo(200.0);
        assertThat(existing.getType()).isEqualTo(TransactionType.EXPENSE);
        assertThat(existing.getDescription()).isEqualTo("Aluguel");
    }

    @Test
    @DisplayName("Should throw exception when transaction not found on update")
    void updateTransactionById_ShouldThrowException_WhenNotFound() {
        TransactionRequestDto dto = new TransactionRequestDto(
                200.0, TransactionType.EXPENSE, LocalDate.now(), "Aluguel");

        when(transactionRepository.findByIdAndUserId(any(), any()))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> transactionService.updateTransactionById(1, dto, 1));

        verify(transactionRepository, never()).save(any());
    }
}