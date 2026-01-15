package com.vinicius.finance_api.Repositories;

import com.vinicius.finance_api.Entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
}
