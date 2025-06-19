package com.finquik.controllers;

import com.finquik.DTOs.TransactionRequest;
import com.finquik.DTOs.TransactionResponse;
import com.finquik.models.CategoryType;
import com.finquik.services.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(
            @Valid @RequestBody TransactionRequest transactionRequest,
            Authentication authentication) {

        String userEmail = authentication.getName();
        TransactionResponse createdTransaction = transactionService.createTransaction(transactionRequest, userEmail);
        return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getUserTransactions(
            Authentication authentication,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Long accountId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) CategoryType type) {

        String userEmail = authentication.getName();
        List<TransactionResponse> transactions = transactionService.getTransactions(
                userEmail, startDate, endDate, accountId, categoryId, type);

        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getTransactionById(
            @PathVariable Long id,
            Authentication authentication) {

        String userEmail = authentication.getName();
        TransactionResponse transaction = transactionService.getTransactionById(id, userEmail);
        return ResponseEntity.ok(transaction);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponse> updateTransaction(
            @PathVariable Long id,
            @Valid @RequestBody TransactionRequest transactionRequest,
            Authentication authentication) {

        String userEmail = authentication.getName();
        TransactionResponse updatedTransaction = transactionService.updateTransaction(id, transactionRequest, userEmail);
        return ResponseEntity.ok(updatedTransaction);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(
            @PathVariable Long id,
            Authentication authentication) {

        String userEmail = authentication.getName();
        transactionService.deleteTransaction(id, userEmail);
        return ResponseEntity.noContent().build();
    }
}
