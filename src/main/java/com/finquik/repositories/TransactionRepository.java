package com.finquik.repositories;

import com.finquik.models.Account;
import com.finquik.models.Transaction;
import com.finquik.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Transaction entities.
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

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

    //TODO: add complex methods for future features
}
