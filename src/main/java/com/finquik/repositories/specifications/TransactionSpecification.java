package com.finquik.repositories.specifications;

import com.finquik.models.*;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDate;

public class TransactionSpecification {

    /**
     * Specification to filter transactions by user.
     */
    public static Specification<Transaction> hasUser(User user) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("user"), user);
    }

    /**
     * Specification to filter transactions by account ID.
     */
    public static Specification<Transaction> hasAccountId(Long accountId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("account").get("id"), accountId);
    }

    /**
     * Specification to filter transactions by category ID.
     */
    public static Specification<Transaction> hasCategoryId(Long categoryId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("category").get("id"), categoryId);
    }

    /**
     * Specification to filter transactions by type (INCOME or EXPENSE).
     * This requires a JOIN with the Category table.
     */
    public static Specification<Transaction> hasType(CategoryType type) {
        return (root, query, criteriaBuilder) -> {
            Join<Transaction, Category> categoryJoin = root.join("category");
            return criteriaBuilder.equal(categoryJoin.get("type"), type);
        };
    }

    /**
     * Specification to filter transactions with a date greater than or equal to the start date.
     */
    public static Specification<Transaction> isAfterOrEqualTo(LocalDate startDate) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("transactionDate"), startDate);
    }

    /**
     * Specification to filter transactions with a date less than or equal to the end date.
     */
    public static Specification<Transaction> isBeforeOrEqualTo(LocalDate endDate) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("transactionDate"), endDate);
    }

    /**
     * Specification to filter transactions by a description containing a specific text (case-insensitive).
     */
    public static Specification<Transaction> descriptionContains(String text) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("description")),
                        "%" + text.toLowerCase() + "%"
                );
    }

}
