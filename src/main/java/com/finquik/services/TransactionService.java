package com.finquik.services;

import com.finquik.DTOs.TransactionRequest;
import com.finquik.DTOs.TransactionResponse;
import com.finquik.models.CategoryType;

import java.time.LocalDate;
import java.util.List;

public interface TransactionService {

    /**
     * Creates a new transaction for the authenticated user and updates the corresponding account balance.
     *
     * @param transactionRequest DTO with the transaction details.
     * @param userEmail The email of the authenticated user.
     * @return The created transaction information.
     */
    TransactionResponse createTransaction(TransactionRequest transactionRequest, String userEmail);

    /**
     * Retrieves a list of transactions for the authenticated user, applying optional filters.
     *
     * @param userEmail The email of the authenticated user.
     * @param startDate Optional start date for the filter range.
     * @param endDate Optional end date for the filter range.
     * @param accountId Optional account ID to filter by.
     * @param categoryId Optional category ID to filter by.
     * @param type Optional transaction type (INCOME or EXPENSE) to filter by.
     * @return A filtered and sorted list of transaction information.
     */
    List<TransactionResponse> getTransactions(String userEmail, LocalDate startDate, LocalDate endDate, Long accountId, Long categoryId, CategoryType type);

    /**
     * Retrieves a single transaction by its ID, ensuring it belongs to the authenticated user.
     *
     * @param transactionId The ID of the transaction to retrieve.
     * @param userEmail The email of the authenticated user.
     * @return The transaction information.
     */
    TransactionResponse getTransactionById(Long transactionId, String userEmail);

    /**
     * Updates an existing transaction and recalculates the corresponding account balance.
     *
     * @param transactionId The ID of the transaction to update.
     * @param transactionRequest DTO with the new transaction details.
     * @param userEmail The email of the authenticated user.
     * @return The updated transaction information.
     */
    TransactionResponse updateTransaction(Long transactionId, TransactionRequest transactionRequest, String userEmail);

    /**
     * Deletes a transaction by its ID and reverts its impact on the corresponding account balance.
     *
     * @param transactionId The ID of the transaction to delete.
     * @param userEmail The email of the authenticated user.
     */
    void deleteTransaction(Long transactionId, String userEmail);
}
