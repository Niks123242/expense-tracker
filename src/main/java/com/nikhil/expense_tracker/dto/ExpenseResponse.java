package com.nikhil.expense_tracker.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class ExpenseResponse {

    private UUID id;
    private BigDecimal amount;
    private String category;
    private String description;
    private LocalDate expenseDate;
    private LocalDateTime createdAt;
}
