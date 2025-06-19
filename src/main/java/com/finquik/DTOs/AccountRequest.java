package com.finquik.DTOs;

import com.finquik.models.AccountType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountRequest {

    @NotBlank(message = "Account name cannot be blank")
    @Size(min = 2, max = 100, message = "Account name must be between 2 and 100 characters")
    private String name;

    @NotNull(message = "Account type cannot be null")
    private AccountType type;

    @NotNull(message = "Initial balance cannot be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Initial balance must be zero or positive")
    private BigDecimal initialBalance;

    @NotBlank(message = "Currency code cannot be blank")
    @Size(min = 3, max = 3, message = "Currency code must be 3 characters long (e.g., ARS, USD)")
    private String currency;
}
