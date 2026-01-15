package com.vinicius.finance_api.Service;

import com.vinicius.finance_api.Dto.TransactionRequestDto;
import com.vinicius.finance_api.Entities.Transaction;
import com.vinicius.finance_api.Repositories.TransactionRepository;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    public final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }


 public void saveTransaction(TransactionRequestDto transaction) {
     Transaction newTransaction = new Transaction(
             null,
                transaction.value(),
                transaction.type(),
                transaction.date(),
                transaction.description()
     );
        transactionRepository.save(newTransaction);
 }

}
