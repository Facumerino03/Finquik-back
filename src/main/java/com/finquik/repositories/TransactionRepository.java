package com.finquik.repositories;

import com.finquik.models.Account;
import com.finquik.models.Transaction;
import com.finquik.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
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
    //TODO: add complex methods for future features
}
