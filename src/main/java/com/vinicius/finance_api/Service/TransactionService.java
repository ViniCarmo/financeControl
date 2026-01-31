package com.vinicius.finance_api.Service;

import com.vinicius.finance_api.Dto.TransactionRequestDto;
import com.vinicius.finance_api.Dto.TransactionResponseDto;
import com.vinicius.finance_api.Entities.Transaction;
import com.vinicius.finance_api.Entities.User;
import com.vinicius.finance_api.Repositories.TransactionRepository;
import com.vinicius.finance_api.Repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    public final TransactionRepository transactionRepository;
    public final UserRepository userRepository;

    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

 public void saveTransaction(TransactionRequestDto transaction) {
     User user = userRepository.findById(transaction.userId())
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

 public List<TransactionResponseDto> getAllTransactions() {
     return transactionRepository.findAll()
             .stream()
             .map(transaction -> new TransactionResponseDto(
                     transaction.getId(),
                     transaction.getValue(),
                     transaction.getType(),
                     transaction.getDate(),
                     transaction.getDescription(),
                     transaction.getUser().getId()
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
                transaction.getDescription(),
                transaction.getUser().getId()
        );
 }

    public List<TransactionResponseDto> getTransactionsByUserId(Integer userId) {
        return transactionRepository.findByUserId(userId)
                .stream()
                .map(t -> new TransactionResponseDto(
                        t.getId(),
                        t.getValue(),
                        t.getType(),
                        t.getDate(),
                        t.getDescription(),
                        t.getUser().getId()
                )).toList();
    }

 public void deleteTransactionById(Integer id){
        if (!transactionRepository.existsById(id)){
            throw new RuntimeException("Transaction not found");
        }
        transactionRepository.deleteById(id);
 }

 public void updateTransactionById (Integer id, TransactionRequestDto transaction){
       Transaction existTransaction = transactionRepository.findById(id).orElseThrow(() -> new RuntimeException("Transaction not found"));

       User user = userRepository.findById(transaction.userId())
               .orElseThrow(() -> new RuntimeException("User not found"));

         Transaction transactionUpdate = new Transaction(
                existTransaction.getId(),
                transaction.value() != null ? transaction.value() : existTransaction.getValue(),
                transaction.type() != null ? transaction.type() : existTransaction.getType(),
                transaction.date() != null ? transaction.date() : existTransaction.getDate(),
                transaction.description() != null ? transaction.description() : existTransaction.getDescription(),
                 user
         );
            transactionRepository.saveAndFlush(transactionUpdate);
 }

}
