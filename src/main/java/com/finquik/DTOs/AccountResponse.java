package com.finquik.DTOs;

import com.finquik.models.AccountType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class AccountResponse {

    private Long id;
    private String name;
    private AccountType type;
    private BigDecimal currentBalance;
    private String currency;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}