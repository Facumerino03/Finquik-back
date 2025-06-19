package com.finquik.services;

import com.finquik.DTOs.TransactionRequest;
import com.finquik.DTOs.TransactionResponse;

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
     * Retrieves all transactions for the authenticated user.
     *
     * @param userEmail The email of the authenticated user.
     * @return A list of all transactions belonging to the user, ordered by date.
     */
    List<TransactionResponse> getTransactionsByUser(String userEmail);

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

    //TODO: methods for getById, update, and delete transactions will be added later to focus on the main logic.
}
