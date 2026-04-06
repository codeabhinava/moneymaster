package com.example.moneymaster.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.moneymaster.model.FinancialRecord;
import com.example.moneymaster.model.RecordRegistration;
import com.example.moneymaster.model.TransactionType;
import com.example.moneymaster.repository.FinancialRecordRepository;

@Service
public class RecordService {

    private final FinancialRecordRepository financialRecordRepository;
    private final AppService appService;

    public RecordService(FinancialRecordRepository financialRecordRepository, AppService appService) {
        this.financialRecordRepository = financialRecordRepository;
        this.appService = appService;
    }

    public FinancialRecord saveRecord(UUID token, RecordRegistration recordRegistration) {
        if (recordRegistration.getType() == TransactionType.EXPENSE && recordRegistration.getAmount() < 0) {
            throw new IllegalArgumentException("Expense amount must be positive");
        }
        if (recordRegistration.getType() == TransactionType.INCOME && recordRegistration.getAmount() < 0) {
            throw new IllegalArgumentException("Income amount must be positive");
        }
        FinancialRecord record = new FinancialRecord(
                recordRegistration.getAmount(),
                recordRegistration.getType(),
                recordRegistration.getCategory(),
                LocalDateTime.now(),
                recordRegistration.getDescription(),
                appService.getUserByToken(token));
        return financialRecordRepository.save(record);
    }

    public List<FinancialRecord> viewUserRecord(UUID token) {
        List<FinancialRecord> record = financialRecordRepository.findByAppUserId(appService.getUserByToken(token));
        if (record.isEmpty()) {
            throw new IllegalArgumentException("No financial record found for the user");
        }
        return record;

    }

    public FinancialRecord updateUserRecord(UUID token, RecordRegistration recordRegistration, Long id) {
        FinancialRecord existingRecord = financialRecordRepository.findByAppUserIdandId(appService.getUserByToken(token), id);

        if (existingRecord.getAmount() != recordRegistration.getAmount()) {
            existingRecord.setAmount(recordRegistration.getAmount());

        }
        if ((existingRecord.getType() != recordRegistration.getType()) && recordRegistration.getType() != null) {
            existingRecord.setType(recordRegistration.getType());
        }
        if (!existingRecord.getCategory().equals(recordRegistration.getCategory()) && recordRegistration.getCategory() != null) {
            existingRecord.setCategory(recordRegistration.getCategory());
        }

        if (!existingRecord.getDescription().equals(recordRegistration.getDescription()) && recordRegistration.getDescription() != null) {
            existingRecord.setDescription(recordRegistration.getDescription());
        }
        return financialRecordRepository.save(existingRecord);
    }

    public FinancialRecord deleteUserRecord(UUID token, Long id) {
        FinancialRecord existingRecord = financialRecordRepository.findByAppUserIdandId(appService.getUserByToken(token), id);
        financialRecordRepository.delete(existingRecord);
        return existingRecord;
    }

    public List<FinancialRecord> viewUserRecordByCategory(UUID token, String category) {
        List<FinancialRecord> record = financialRecordRepository.findByCategoryAndAppUserId(category, appService.getUserByToken(token));
        if (record.isEmpty()) {
            throw new IllegalArgumentException("No financial record found for the user with the specified category");
        }
        return record;
    }

    public List<FinancialRecord> viewUserRecordByDate(UUID token, String startDate, String endDate) {
        List<FinancialRecord> record = financialRecordRepository.findByUserAndDateRange(appService.getUserByToken(token), LocalDateTime.parse(startDate), LocalDateTime.parse(endDate));
        if (record.isEmpty()) {
            throw new IllegalArgumentException("No financial record found for the user with the specified date range");
        }
        return record;
    }

    public List<FinancialRecord> viewUserRecordByType(UUID token, TransactionType type) {
        List<FinancialRecord> record = financialRecordRepository.findByTypeAndAppUserId(type, appService.getUserByToken(token));
        if (record.isEmpty()) {
            throw new IllegalArgumentException("No financial record found for the user with the specified type");
        }
        return record;
    }

    public List<FinancialRecord> viewUserRecordByCategoryAndDate(UUID token, String category, String startDate,
            String endDate) {
        List<FinancialRecord> record = financialRecordRepository.findByUserAndCategoryAndDateRange(appService.getUserByToken(token), category, LocalDateTime.parse(startDate), LocalDateTime.parse(endDate));
        if (record.isEmpty()) {
            throw new IllegalArgumentException("No financial record found for the user with the specified category and date range");
        }
        return record;
    }

    public List<FinancialRecord> viewUserRecordByCategoryAndTypeAndDate(UUID token, String category, String type,
            String startDate, String endDate) {
        return financialRecordRepository.findByUserAndCategoryAndTypeAndDateRange(appService.getUserByToken(token), category, TransactionType.valueOf(type), LocalDateTime.parse(startDate), LocalDateTime.parse(endDate));

    }

    public List<FinancialRecord> viewUserRecordByDateAndId(UUID token, Long id) {
        return financialRecordRepository.findByDateAndAppUserId(LocalDate.now(), appService.getUserByToken(token));
    }

    public Double viewTotalExpense(UUID token) {
        return financialRecordRepository.findTotalExpenseByAppUserId(appService.getUserByToken(token));
    }

    public Double viewTotalIncome(UUID token) {
        return financialRecordRepository.findTotalIncomeByAppUserId(appService.getUserByToken(token));
    }

    public Double viewTotalByType(UUID token, TransactionType type) {
        return financialRecordRepository.findTotalByAppUserIdAndType(appService.getUserByToken(token), type);
    }

    public Double viewTotalByCategory(UUID token, String category) {
        return financialRecordRepository.findTotalByAppUserIdAndCategory(appService.getUserByToken(token), category);
    }

    public Double viewTotalByTypeAndCategory(UUID token, String type, String category) {
        return financialRecordRepository.findTotalByAppUserIdAndTypeAndCategory(appService.getUserByToken(token), TransactionType.valueOf(type), category);
    }

}
