package com.finquik.controllers;

import com.finquik.DTOs.TransactionRequest;
import com.finquik.DTOs.TransactionResponse;
import com.finquik.services.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<TransactionResponse>> getUserTransactions(Authentication authentication) {
        String userEmail = authentication.getName();
        List<TransactionResponse> transactions = transactionService.getTransactionsByUser(userEmail);
        return ResponseEntity.ok(transactions);
    }
}