package com.finquik.DTOs;

import java.math.BigDecimal;

public record TransactionSummaryDTO(
        BigDecimal totalIncome,
        BigDecimal totalExpenses
) {}
