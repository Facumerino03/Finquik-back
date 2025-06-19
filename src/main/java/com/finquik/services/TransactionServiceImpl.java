package com.finquik.services;

import com.finquik.common.exceptions.ResourceNotFoundException;
import com.finquik.DTOs.AccountResponse;
import com.finquik.DTOs.CategoryResponse;
import com.finquik.DTOs.TransactionRequest;
import com.finquik.DTOs.TransactionResponse;
import com.finquik.models.*;
import com.finquik.repositories.AccountRepository;
import com.finquik.repositories.CategoryRepository;
import com.finquik.repositories.TransactionRepository;
import com.finquik.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional // all the operations in this method are part of a single transaction
    public TransactionResponse createTransaction(TransactionRequest transactionRequest, String userEmail) {
        // 1. Get the user, account, and category based on the request
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", userEmail));

        Account account = accountRepository.findByIdAndUser(transactionRequest.getAccountId(), user)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "id", transactionRequest.getAccountId()));

        Category category = categoryRepository.findByIdAndUser(transactionRequest.getCategoryId(), user)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", transactionRequest.getCategoryId()));

        // 2. Create the transaction entity
        Transaction transaction = Transaction.builder()
                .amount(transactionRequest.getAmount())
                .description(transactionRequest.getDescription())
                .transactionDate(transactionRequest.getTransactionDate())
                .user(user)
                .account(account)
                .category(category)
                .build();

        // 3. Update the account balance
        BigDecimal newBalance;
        if (category.getType() == CategoryType.INCOME) {
            newBalance = account.getCurrentBalance().add(transaction.getAmount());
        } else { // EXPENSE
            newBalance = account.getCurrentBalance().subtract(transaction.getAmount());
        }
        account.setCurrentBalance(newBalance);

        Transaction savedTransaction = transactionRepository.save(transaction);

        // 5. Map the saved transaction to a response DTO
        return mapToTransactionResponse(savedTransaction);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponse> getTransactionsByUser(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", userEmail));

        List<Transaction> transactions = transactionRepository.findByUserOrderByTransactionDateDesc(user);

        return transactions.stream()
                .map(this::mapToTransactionResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionResponse getTransactionById(Long transactionId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", userEmail));

        Transaction transaction = transactionRepository.findByIdAndUser(transactionId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction", "id", transactionId));

        return mapToTransactionResponse(transaction);
    }

    @Override
    @Transactional
    public TransactionResponse updateTransaction(Long transactionId, TransactionRequest transactionRequest, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", userEmail));

        Transaction transactionToUpdate = transactionRepository.findByIdAndUser(transactionId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction", "id", transactionId));

        Account originalAccount = transactionToUpdate.getAccount();
        BigDecimal originalAmount = transactionToUpdate.getAmount();
        CategoryType originalType = transactionToUpdate.getCategory().getType();

        // 1. Revert the impact of the original transaction on the account balance.
        if (originalType == CategoryType.INCOME) {
            originalAccount.setCurrentBalance(originalAccount.getCurrentBalance().subtract(originalAmount));
        } else { // EXPENSE
            originalAccount.setCurrentBalance(originalAccount.getCurrentBalance().add(originalAmount));
        }

        // 2. Obtain the new account and category entities safely.
        Account targetAccount = accountRepository.findByIdAndUser(transactionRequest.getAccountId(), user)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "id", transactionRequest.getAccountId()));
        Category targetCategory = categoryRepository.findByIdAndUser(transactionRequest.getCategoryId(), user)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", transactionRequest.getCategoryId()));

        // If the account has changed, we need to save the original account with its reverted balance.
        if (!originalAccount.getId().equals(targetAccount.getId())) {
            accountRepository.save(originalAccount);
        }

        // 3. Update the transaction with the new data.
        transactionToUpdate.setAmount(transactionRequest.getAmount());
        transactionToUpdate.setDescription(transactionRequest.getDescription());
        transactionToUpdate.setTransactionDate(transactionRequest.getTransactionDate());
        transactionToUpdate.setAccount(targetAccount);
        transactionToUpdate.setCategory(targetCategory);

        // 4. Apply the new impact on the target account balance.
        BigDecimal newAmount = transactionToUpdate.getAmount();
        if (targetCategory.getType() == CategoryType.INCOME) {
            targetAccount.setCurrentBalance(targetAccount.getCurrentBalance().add(newAmount));
        } else { // EXPENSE
            targetAccount.setCurrentBalance(targetAccount.getCurrentBalance().subtract(newAmount));
        }
        accountRepository.save(targetAccount);

        // 5. Save the updated transaction
        Transaction updatedTransaction = transactionRepository.save(transactionToUpdate);

        return mapToTransactionResponse(updatedTransaction);
    }

    // Auxiliary methods to map the entity to the response DTO
    private TransactionResponse mapToTransactionResponse(Transaction transaction) {
        AccountResponse accountResponse = AccountResponse.builder()
                .id(transaction.getAccount().getId())
                .name(transaction.getAccount().getName())
                .type(transaction.getAccount().getType())
                .currency(transaction.getAccount().getCurrency())
                .currentBalance(transaction.getAccount().getCurrentBalance())
                .createdAt(transaction.getAccount().getCreatedAt())
                .updatedAt(transaction.getAccount().getUpdatedAt())
                .build();

        CategoryResponse categoryResponse = CategoryResponse.builder()
                .id(transaction.getCategory().getId())
                .name(transaction.getCategory().getName())
                .type(transaction.getCategory().getType())
                .build();

        return TransactionResponse.builder()
                .id(transaction.getId())
                .amount(transaction.getAmount())
                .description(transaction.getDescription())
                .transactionDate(transaction.getTransactionDate())
                .createdAt(transaction.getCreatedAt())
                .account(accountResponse)
                .category(categoryResponse)
                .build();
    }
}
