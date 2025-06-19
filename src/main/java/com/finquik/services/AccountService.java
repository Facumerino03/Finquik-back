package com.finquik.services;

import com.finquik.DTOs.AccountRequest;
import com.finquik.DTOs.AccountResponse;

import java.util.List;

public interface AccountService {

    /**
     * Creates a new account for the currently authenticated user.
     *
     * @param accountRequest DTO with the account details.
     * @param userEmail The email of the authenticated user.
     * @return The created account information.
     */
    AccountResponse createAccount(AccountRequest accountRequest, String userEmail);

    /**
     * Retrieves all accounts for the currently authenticated user.
     *
     * @param userEmail The email of the authenticated user.
     * @return A list of all accounts belonging to the user.
     */
    List<AccountResponse> getAccountsByUser(String userEmail);

    /**
     * Retrieves a single account by its ID, ensuring it belongs to the authenticated user.
     *
     * @param accountId The ID of the account to retrieve.
     * @param userEmail The email of the authenticated user.
     * @return The account information.
     */
    AccountResponse getAccountById(Long accountId, String userEmail);

    /**
     * Updates an existing account, ensuring it belongs to the authenticated user.
     *
     * @param accountId The ID of the account to update.
     * @param accountRequest DTO with the new account details.
     * @param userEmail The email of the authenticated user.
     * @return The updated account information.
     */
    AccountResponse updateAccount(Long accountId, AccountRequest accountRequest, String userEmail);

    /**
     * Deletes an account, ensuring it belongs to the authenticated user.
     *
     * @param accountId The ID of the account to delete.
     * @param userEmail The email of the authenticated user.
     */
    void deleteAccount(Long accountId, String userEmail);
}