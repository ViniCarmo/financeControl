package com.vinicius.finance_api.Service;

import com.vinicius.finance_api.Dto.TransactionRequestDto;
import com.vinicius.finance_api.Dto.TransactionResponseDto;
import com.vinicius.finance_api.Entities.Transaction;
import com.vinicius.finance_api.Repositories.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

 public List<TransactionResponseDto> getAllTransactions() {
     return transactionRepository.findAll()
             .stream()
             .map(transaction -> new TransactionResponseDto(
                     transaction.getId(),
                     transaction.getValue(),
                     transaction.getType(),
                     transaction.getDate(),
                     transaction.getDescription()
             )).toList();
 }

 public TransactionResponseDto getTransactionById (Integer id){
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        return new TransactionResponseDto(
                transaction.getId(),
                transaction.getValue(),
                transaction.getType(),
                transaction.getDate(),
                transaction.getDescription()
        );
 }

 public void deleteTransactionById(Integer id){
        if (!transactionRepository.existsById(id)){
            throw new RuntimeException("Transaction not found");
        }
        transactionRepository.deleteById(id);
 }

 public void updateTransactionById (Integer id, TransactionRequestDto transaction){
       Transaction existTransaction = transactionRepository.findById(id).orElseThrow(() -> new RuntimeException("Transaction not found"));
         Transaction transactionUpdate = new Transaction(
                existTransaction.getId(),
                transaction.value() != null ? transaction.value() : existTransaction.getValue(),
                transaction.type() != null ? transaction.type() : existTransaction.getType(),
                transaction.date() != null ? transaction.date() : existTransaction.getDate(),
                transaction.description() != null ? transaction.description() : existTransaction.getDescription()
         );
            transactionRepository.saveAndFlush(transactionUpdate);
 }

}
