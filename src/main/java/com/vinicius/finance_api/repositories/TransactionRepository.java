package com.vinicius.finance_api.repositories;

import com.vinicius.finance_api.entity.Transaction;
import com.vinicius.finance_api.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    Page<Transaction> findByUserId(Integer userId, Pageable pageable);
    Optional<Transaction> findByIdAndUserId(Integer id, Integer userId);
    List<Transaction> findByUserId(Integer userId);
    List<Transaction> findByUserIdAndDateBetween(Integer userId, LocalDate start, LocalDate finish);
    Page<Transaction> findByUserIdAndType(Integer userId, TransactionType type, Pageable pageable);
}
