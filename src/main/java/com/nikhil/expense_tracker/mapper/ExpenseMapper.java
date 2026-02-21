package com.nikhil.expense_tracker.mapper;

import com.nikhil.expense_tracker.dto.ExpenseResponse;
import com.nikhil.expense_tracker.entity.Expense;
import org.springframework.stereotype.Component;

@Component
public class ExpenseMapper {
    public ExpenseResponse toResponse(Expense expense) {

        return ExpenseResponse.builder()
                .id(expense.getId())
                .amount(expense.getAmount())
                .category(expense.getCategory())
                .description(expense.getDescription())
                .expenseDate(expense.getExpenseDate())
                .createdAt(expense.getCreatedAt())
                .build();
    }
}
