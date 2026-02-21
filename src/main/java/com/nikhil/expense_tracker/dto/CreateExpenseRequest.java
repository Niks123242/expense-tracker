package com.nikhil.expense_tracker.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class CreateExpenseRequest {

    private BigDecimal amount;
    private String category;
    private String description;
    private LocalDate expenseDate;
}
