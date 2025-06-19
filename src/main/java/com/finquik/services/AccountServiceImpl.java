package com.finquik.services;

import com.finquik.common.exceptions.ResourceNotFoundException;
import com.finquik.DTOs.AccountRequest;
import com.finquik.DTOs.AccountResponse;
import com.finquik.models.Account;
import com.finquik.models.User;
import com.finquik.repositories.AccountRepository;
import com.finquik.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public AccountResponse createAccount(AccountRequest accountRequest, String userEmail) {
        User user = findUserByEmail(userEmail);

        Account account = Account.builder()
                .name(accountRequest.getName())
                .type(accountRequest.getType())
                .initialBalance(accountRequest.getInitialBalance())
                .currentBalance(accountRequest.getInitialBalance())
                .currency(accountRequest.getCurrency().toUpperCase())
                .user(user)
                .build();

        Account savedAccount = accountRepository.save(account);

        return mapToAccountResponse(savedAccount);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountResponse> getAccountsByUser(String userEmail) {
        User user = findUserByEmail(userEmail);
        List<Account> accounts = accountRepository.findByUser(user);
        return accounts.stream()
                .map(this::mapToAccountResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AccountResponse getAccountById(Long accountId, String userEmail) {
        User user = findUserByEmail(userEmail);
        Account account = accountRepository.findByIdAndUser(accountId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "id", accountId));

        return mapToAccountResponse(account);
    }

    // auxiliary private methods to reuse code and keep public methods cleaner
    private AccountResponse mapToAccountResponse(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .name(account.getName())
                .type(account.getType())
                .currentBalance(account.getCurrentBalance())
                .currency(account.getCurrency())
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .build();
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }
}