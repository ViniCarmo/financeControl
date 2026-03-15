package com.vinicius.finance_api.controller;

import com.vinicius.finance_api.dto.TransactionRequestDto;
import com.vinicius.finance_api.dto.TransactionResponseDto;
import com.vinicius.finance_api.entity.User;
import com.vinicius.finance_api.service.TransactionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
@SecurityRequirement(name = "bearer-key")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<Void> saveTransaction(@AuthenticationPrincipal User loggerUser,
                                                @RequestBody @Valid TransactionRequestDto transactionRequestDto) {
        transactionService.saveTransaction(transactionRequestDto, loggerUser.getId());
        return ResponseEntity.status(201).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@AuthenticationPrincipal User loggedUser,
                                                  @PathVariable Integer id) {
        transactionService.deleteTransactionById(loggedUser.getId(),id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Page<TransactionResponseDto>> getAllTransactions(@AuthenticationPrincipal User loggedUser,
                                                                           @RequestParam(defaultValue = "0") int page,
                                                                           @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(transactionService.getAllTransactions(loggedUser.getId(), PageRequest.of(page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDto> getById(@AuthenticationPrincipal User loggedUser,
                                                          @PathVariable Integer id) {
        return ResponseEntity.ok(transactionService.getTransactionById(id, loggedUser.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTransaction(@AuthenticationPrincipal User loggedUser,
                                                  @PathVariable Integer id,
                                                  @RequestBody TransactionRequestDto transactionRequestDto) {
        transactionService.updateTransactionById(id, transactionRequestDto, loggedUser.getId());
        return ResponseEntity.ok().build();
    }


}
