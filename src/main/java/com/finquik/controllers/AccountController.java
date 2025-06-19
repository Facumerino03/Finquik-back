package com.finquik.controllers;

import com.finquik.DTOs.AccountRequest;
import com.finquik.DTOs.AccountResponse;
import com.finquik.services.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(
            @Valid @RequestBody AccountRequest accountRequest,
            Authentication authentication) {

        String userEmail = authentication.getName();
        AccountResponse createdAccount = accountService.createAccount(accountRequest, userEmail);
        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getUserAccounts(Authentication authentication) {
        String userEmail = authentication.getName();
        List<AccountResponse> accounts = accountService.getAccountsByUser(userEmail);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccountById(
            @PathVariable Long id,
            Authentication authentication) {

        String userEmail = authentication.getName();
        AccountResponse account = accountService.getAccountById(id, userEmail);
        return ResponseEntity.ok(account);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountResponse> updateAccount(
            @PathVariable Long id,
            @Valid @RequestBody AccountRequest accountRequest,
            Authentication authentication) {

        String userEmail = authentication.getName();
        AccountResponse updatedAccount = accountService.updateAccount(id, accountRequest, userEmail);
        return ResponseEntity.ok(updatedAccount);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(
            @PathVariable Long id,
            Authentication authentication) {

        String userEmail = authentication.getName();
        accountService.deleteAccount(id, userEmail);
        return ResponseEntity.noContent().build();
    }
}
