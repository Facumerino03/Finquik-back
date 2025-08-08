package com.finquik.repositories;

import com.finquik.DTOs.TransactionSummaryDTO;
import com.finquik.models.Account;
import com.finquik.models.Transaction;
import com.finquik.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Transaction entities.
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {

    /**
     * Finds all transactions belonging to a specific user, ordered by transaction date descending.
     *
     * @param user The user whose transactions to find.
     * @return A list of transactions for the given user.
     */
    List<Transaction> findByUserOrderByTransactionDateDesc(User user);

    /**
     * Finds all transactions associated with a specific account.
     *
     * @param account The account to find transactions for.
     * @return A list of transactions for the given account.
     */
    List<Transaction> findByAccount(Account account);

    /**
     * Finds a specific transaction by its ID and the user who owns it.
     *
     * @param id The ID of the transaction.
     * @param user The user owner.
     * @return an {@link Optional} containing the transaction if found and owned by the user.
     */
    Optional<Transaction> findByIdAndUser(Long id, User user);

    /**
     * Calculates the total income and total expenses for a specific user directly in the database.
     * This query joins to the Category to check its type (INCOME/EXPENSE) and then sums the transaction amounts.
     *
     * @param userId The ID of the user for whom to calculate the summary.
     * @return an {@link Optional} containing the {@link TransactionSummaryDTO} with total income and expenses.
     */
    @Query("""
        SELECT new com.finquik.DTOs.TransactionSummaryDTO(
            COALESCE(SUM(CASE WHEN t.category.type = com.finquik.models.CategoryType.INCOME THEN t.amount ELSE 0 END), 0),
            COALESCE(SUM(CASE WHEN t.category.type = com.finquik.models.CategoryType.EXPENSE THEN t.amount ELSE 0 END), 0)
        )
        FROM Transaction t
        WHERE t.account.user.id = :userId
    """)
    Optional<TransactionSummaryDTO> getTransactionSummaryByUserId(@Param("userId") Long userId);

    //TODO: add complex methods for future features
}
