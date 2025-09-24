package com.finquik.repositories;

import com.finquik.models.Category;
import com.finquik.models.CategoryType;
import com.finquik.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Category entities.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Finds all categories belonging to a specific user.
     *
     * @param user The user whose categories to find.
     * @return A list of categories for the given user.
     */
    List<Category> findByUser(User user);

    /**
     * Finds a specific category by its ID and the user who owns it.
     * Useful for security checks.
     *
     * @param id The ID of the category.
     * @param user The user owner.
     * @return an {@link Optional} containing the category if found and owned by the user.
     */
    Optional<Category> findByIdAndUser(Long id, User user);

    /**
     * Checks if a category with the same name, type, and user already exists.
     * Useful to prevent duplicate categories for a user (e.g., two "Food" expense categories).
     *
     * @param name The name of the category.
     * @param user The user owner.
     * @param type The type of the category (INCOME or EXPENSE).
     * @return true if a matching category exists, false otherwise.
     */
    boolean existsByNameAndUserAndType(String name, User user, CategoryType type);

    /**
     * Finds all categories belonging to a specific user and matching a specific type.
     *
     * @param user The user whose categories to find.
     * @param type The type of the category (INCOME or EXPENSE).
     * @return A list of categories for the given user and type.
     */
    List<Category> findByUserAndType(User user, CategoryType type);
}