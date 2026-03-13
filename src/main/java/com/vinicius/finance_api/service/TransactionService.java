package com.vinicius.finance_api.service;

import com.vinicius.finance_api.dto.TransactionRequestDto;
import com.vinicius.finance_api.dto.TransactionResponseDto;
import com.vinicius.finance_api.entity.Transaction;
import com.vinicius.finance_api.entity.User;
import com.vinicius.finance_api.repositories.TransactionRepository;
import com.vinicius.finance_api.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    public final TransactionRepository transactionRepository;
    public final UserRepository userRepository;

    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    public void saveTransaction(TransactionRequestDto transaction, Integer userid) {
        User user = userRepository.findById(userid)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Transaction newTransaction = new Transaction(
                null,
                transaction.value(),
                transaction.type(),
                transaction.date(),
                transaction.description(),
                user
        );
        transactionRepository.save(newTransaction);
    }

    public Page<TransactionResponseDto> getAllTransactions(Integer userId, Pageable pageable) {
        return transactionRepository.findByUserId(userId, pageable)
                .map(transaction -> new TransactionResponseDto(
                        transaction.getId(),
                        transaction.getValue(),
                        transaction.getType(),
                        transaction.getDate(),
                        transaction.getDescription(),
                        transaction.getUser().getId()
                ));
    }

    public TransactionResponseDto getTransactionById(Integer id, Integer userId) {
        Transaction transaction = transactionRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found"));
        return new TransactionResponseDto(
                transaction.getId(),
                transaction.getValue(),
                transaction.getType(),
                transaction.getDate(),
                transaction.getDescription(),
                transaction.getUser().getId()
        );
    }

    public void deleteTransactionById(Integer id, Integer userId) {
        Transaction transaction = transactionRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found"));
        transactionRepository.delete(transaction);
    }

    public void updateTransactionById(Integer id, TransactionRequestDto transaction, Integer userId) {
        Transaction existTransaction = transactionRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found"));

        existTransaction.setValue(transaction.value() != null ? transaction.value() : existTransaction.getValue());
        existTransaction.setType(transaction.type() != null ? transaction.type() : existTransaction.getType());
        existTransaction.setDate(transaction.date() != null ? transaction.date() : existTransaction.getDate());
        existTransaction.setDescription(transaction.description() != null ? transaction.description() : existTransaction.getDescription());

        transactionRepository.save(existTransaction);
    }

}
