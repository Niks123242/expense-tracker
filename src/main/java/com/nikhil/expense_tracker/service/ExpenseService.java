package com.nikhil.expense_tracker.service;

import com.nikhil.expense_tracker.dto.CreateExpenseRequest;
import com.nikhil.expense_tracker.dto.ExpenseResponse;
import com.nikhil.expense_tracker.entity.Expense;
import com.nikhil.expense_tracker.mapper.ExpenseMapper;
import com.nikhil.expense_tracker.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ExpenseMapper expenseMapper;

    public ExpenseResponse createExpense(UUID userId, CreateExpenseRequest request) {

        Expense expense = Expense.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .amount(request.getAmount())
                .category(request.getCategory())
                .description(request.getDescription())
                .expenseDate(request.getExpenseDate())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Expense expenseSaved = expenseRepository.save(expense);
        return expenseMapper.toResponse(expenseSaved);
    }

    public Expense updateExpense(UUID userId, UUID expenseId, CreateExpenseRequest request) {

        Expense expense = expenseRepository
                .findByIdAndUserId(expenseId, userId)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        expense.setAmount(request.getAmount());
        expense.setCategory(request.getCategory());
        expense.setDescription(request.getDescription());
        expense.setExpenseDate(request.getExpenseDate());
        expense.setUpdatedAt(LocalDateTime.now());

        return expenseRepository.save(expense);
    }

    public void deleteExpense(UUID userId, UUID expenseId) {

        Expense expense = expenseRepository
                .findByIdAndUserId(expenseId, userId)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        expenseRepository.delete(expense);
    }

    @Cacheable(value = "allExpenses", key = "#userId")
    public Page<ExpenseResponse> getExpenses(UUID userId, Pageable pageable) {

        return expenseRepository
                .findByUserId(userId, pageable)
                .map(expenseMapper::toResponse);
    }

    public List<Expense> getUserExpensesBetween(
            UUID userId,
            LocalDate start,
            LocalDate end
    ) {
        return expenseRepository.findByUserIdAndExpenseDateBetween(userId, start, end);
    }

    @Cacheable(value = "monthlyTotals", key = "#userId + '-' + #year + '-' + #month")
    public BigDecimal getMonthlyTotal(UUID userId, int year, int month) {

        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        return expenseRepository.getTotalBetween(userId, start, end);
    }
}
