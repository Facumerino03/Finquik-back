package com.finquik.repositories;

import com.finquik.models.Account;
import com.finquik.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Account entities.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    /**
     * Finds all accounts belonging to a specific user.
     *
     * @param user The user whose accounts to find.
     * @return A list of accounts for the given user.
     */
    List<Account> findByUser(User user);

    /**
     * Finds a specific account by its ID and the user who owns it.
     * This is useful for security checks to ensure a user is accessing their own account.
     *
     * @param id The ID of the account.
     * @param user The user owner.
     * @return an {@link Optional} containing the account if found and owned by the user, otherwise empty.
     */
    Optional<Account> findByIdAndUser(Long id, User user);
}
