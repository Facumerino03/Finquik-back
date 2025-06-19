package com.finquik.DTOs;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class TransactionResponse {

    private Long id;
    private BigDecimal amount;
    private String description;
    private LocalDate transactionDate;

    // Instead of giving back id, we return the full object
    private AccountResponse account;
    private CategoryResponse category;

    private LocalDateTime createdAt;
}
