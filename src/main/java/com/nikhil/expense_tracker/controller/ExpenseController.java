package com.nikhil.expense_tracker.controller;

import com.nikhil.expense_tracker.dto.CreateExpenseRequest;
import com.nikhil.expense_tracker.dto.ExpenseResponse;
import com.nikhil.expense_tracker.entity.Expense;
import com.nikhil.expense_tracker.entity.User;
import com.nikhil.expense_tracker.security.CustomUserDetails;
import com.nikhil.expense_tracker.service.ExpenseService;
import com.nikhil.expense_tracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ExpenseResponse> createExpense(
            Authentication authentication,
            @RequestBody CreateExpenseRequest request) {

        String email = (String) authentication.getPrincipal();

        User user = userService.getCurrentUser(email);

        return ResponseEntity.ok(
                expenseService.createExpense(user.getId(), request)
        );
    }

    @GetMapping
    public ResponseEntity<Page<ExpenseResponse>> getExpenses(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        String email = (String) authentication.getPrincipal();

        User user = userService.getCurrentUser(email);

        Pageable pageable = PageRequest.of(page, size, Sort.by("expenseDate").descending());

        return ResponseEntity.ok(
                expenseService.getExpenses(user.getId(), pageable)
        );
    }

    @GetMapping("/range")
    public ResponseEntity<List<Expense>> getExpensesByRange(
            Authentication authentication,
            @RequestParam LocalDate start,
            @RequestParam LocalDate end) {

        String email = (String) authentication.getPrincipal();

        User user = userService.getCurrentUser(email);

        return ResponseEntity.ok(
                expenseService.getUserExpensesBetween(user.getId(), start, end)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Expense> updateExpense(
            Authentication authentication,
            @PathVariable UUID id,
            @RequestBody CreateExpenseRequest request) {

        String email = (String) authentication.getPrincipal();

        User user = userService.getCurrentUser(email);

        return ResponseEntity.ok(
                expenseService.updateExpense(user.getId(), id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(
            Authentication authentication,
            @PathVariable UUID id) {

        String email = (String) authentication.getPrincipal();

        User user = userService.getCurrentUser(email);

        expenseService.deleteExpense(user.getId(), id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/monthly-total")
    public ResponseEntity<BigDecimal> getMonthlyTotal(
            Authentication authentication,
            @RequestParam int year,
            @RequestParam int month) {

        UUID userId = ((CustomUserDetails) authentication.getPrincipal()).getUserId();

        return ResponseEntity.ok(
                expenseService.getMonthlyTotal(userId, year, month)
        );
    }
}
