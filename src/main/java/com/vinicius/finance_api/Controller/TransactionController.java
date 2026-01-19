package com.vinicius.finance_api.Controller;

import com.vinicius.finance_api.Dto.TransactionRequestDto;
import com.vinicius.finance_api.Service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<Void> saveTransaction(TransactionRequestDto transactionRequestDto) {
        transactionService.saveTransaction(transactionRequestDto);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping
    public ResponseEntity<Void> deleteTransaction(Integer id) {
        transactionService.deleteTransactionById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<TransactionRequestDto>> getAllTransactions() {
        transactionService.getAllTransactions();
        return ResponseEntity.ok().build();
    }


}
