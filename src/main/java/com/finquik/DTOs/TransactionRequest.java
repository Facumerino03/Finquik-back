package com.finquik.DTOs;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TransactionRequest {

    @NotNull(message = "Amount cannot be null")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal amount;

    @Size(max = 255, message = "Description can be up to 255 characters long")
    private String description;

    @NotNull(message = "Transaction date cannot be null")
    @PastOrPresent(message = "Transaction date cannot be in the future")
    private LocalDate transactionDate;

    @NotNull(message = "Account ID cannot be null")
    private Long accountId;

    @NotNull(message = "Category ID cannot be null")
    private Long categoryId;

    @Size(max = 100, message = "Icon name can be up to 100 characters long")
    private String iconName;
}