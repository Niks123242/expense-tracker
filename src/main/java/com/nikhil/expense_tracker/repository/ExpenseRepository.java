package com.nikhil.expense_tracker.repository;

import com.nikhil.expense_tracker.entity.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    Page<Expense> findByUserId(UUID userId, Pageable pageable);

    List<Expense> findByUserIdAndExpenseDateBetween(
            UUID userId,
            LocalDate start,
            LocalDate end
    );

    Optional<Expense> findByIdAndUserId(UUID id, UUID userId);

    @Query("""
       SELECT COALESCE(SUM(e.amount), 0)
       FROM Expense e
       WHERE e.userId = :userId
       AND e.expenseDate BETWEEN :start AND :end
       """)
    BigDecimal getTotalBetween(UUID userId, LocalDate start, LocalDate end);
}
