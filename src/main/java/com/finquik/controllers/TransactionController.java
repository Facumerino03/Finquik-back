package com.finquik.controllers;

import com.finquik.DTOs.TransactionRequest;
import com.finquik.DTOs.TransactionResponse;
import com.finquik.DTOs.TransactionSummaryDTO;
import com.finquik.DTOs.PageResponse;
import com.finquik.models.CategoryType;
import com.finquik.services.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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
    public ResponseEntity<PageResponse<TransactionResponse>> getUserTransactions(
            Authentication authentication,
            Pageable pageable,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Long accountId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) CategoryType type,
            @RequestParam(required = false) String description) {

        String userEmail = authentication.getName();
        Page<TransactionResponse> transactionsPage = transactionService.getTransactions(
                userEmail, pageable, startDate, endDate, accountId, categoryId, type, description);

        PageResponse<TransactionResponse> response = new PageResponse<>(transactionsPage);

        return ResponseEntity.ok(response);
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

    @GetMapping("/summary")
    public ResponseEntity<TransactionSummaryDTO> getTransactionSummary() {
        TransactionSummaryDTO summary = transactionService.getTransactionSummaryForCurrentUser();
        return ResponseEntity.ok(summary);
    }
}
