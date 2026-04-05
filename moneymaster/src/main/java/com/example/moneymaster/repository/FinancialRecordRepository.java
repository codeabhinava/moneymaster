package com.example.moneymaster.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.moneymaster.model.TransactionType;
import com.example.moneymaster.model.AppUser;
import com.example.moneymaster.model.FinancialRecord;

@Repository
@Transactional(readOnly = true)
public interface FinancialRecordRepository extends JpaRepository<FinancialRecord, Long> {

    Optional<FinancialRecord> findByAppUserId(AppUser appUser);

    @Query("Select SUM(r.amount) FROM FinancialRecord r WHERE r.appUser = ?1 AND r.type = 'EXPENSE'")
    Double findTotalExpenseByAppUserId(AppUser appUser);

    @Query("Select SUM(r.amount) FROM FinancialRecord r WHERE r.appUser = ?1 AND r.type = 'INCOME'")
    Double findTotalIncomeByAppUserId(AppUser appUser);

    @Query("Select SUM(r.amount) FROM FinancialRecord r WHERE r.appUser = ?1 AND r.type = ?2")
    Double findTotalByAppUserIdAndType(AppUser appUser, TransactionType type);

    @Query("Select SUM(r.amount) FROM FinancialRecord r WHERE r.appUser = ?1 AND r.type = ?2 AND r.category = ?3")
    Double findTotalByAppUserIdAndTypeAndCategory(AppUser appUser, TransactionType type, String category);

    @Query("SELECT f FROM FinancialRecord f WHERE f.appUser = ?1 AND f.date BETWEEN ?2 AND ?3")
    List<FinancialRecord> findByUserAndDateRange(AppUser user,
            LocalDateTime startDate,
            LocalDateTime endDate);

    @Query("SELECT r FROM FinancialRecord r WHERE r.appUser = ?1 AND r.category = ?2 AND r.type = ?3 AND r.date BETWEEN ?4 AND ?5")
    List<FinancialRecord> findByUserAndCategoryAndTypeAndDateRange(AppUser user,
            String category,
            TransactionType type,
            LocalDateTime startDate,
            LocalDateTime endDate);

    @Query("SELECT r FROM FinancialRecord r WHERE r.appUser = ?1 AND r.category = ?2 AND r.date BETWEEN ?3 AND ?4")
    List<FinancialRecord> findByUserAndCategoryAndDateRange(AppUser user,
            String category,
            LocalDateTime startDate,
            LocalDateTime endDate);

    @Query("Select r From FinancialRecord r WHERE r.appUser = ?1 ")
    Page<FinancialRecord> findByAppUserId(AppUser appUser, Pageable pageable);

    @Query("SELECT r FROM FinancialRecord r WHERE r.category = ?1 AND r.appUser = ?2")
    List<FinancialRecord> findByCategoryAndAppUserId(String category, AppUser appUser);

    @Query("SELECT r FROM FinancialRecord r WHERE r.type = ?1 AND r.appUser = ?2")

    List<FinancialRecord> findByTypeAndAppUserId(TransactionType type, AppUser appUser);

    @Query("SELECT r FROM FinancialRecord r WHERE r.date = ?1 AND r.appUser = ?2")

    List<FinancialRecord> findByDateAndAppUserId(LocalDate date, AppUser appUser);

}
