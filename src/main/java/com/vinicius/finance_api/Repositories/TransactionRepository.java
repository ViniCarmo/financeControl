package com.vinicius.finance_api.Repositories;

import com.vinicius.finance_api.Entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    List<Transaction> findByDateBetween(LocalDate start, LocalDate finish);

    List<Transaction> findByUserId(Integer userId);

    List<Transaction> findByUserIdAndDateBetween(Integer userId, LocalDate start, LocalDate finish);
}
