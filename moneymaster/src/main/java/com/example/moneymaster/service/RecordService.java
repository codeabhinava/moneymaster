package com.example.moneymaster.service;

import java.time.LocalDateTime;
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

    public FinancialRecord viewUserRecord(UUID token) {
        return financialRecordRepository.findByAppUserId(appService.getUserByToken(token)).orElseThrow(() -> new RuntimeException("Record not found for user with token: " + token));
    }

    public FinancialRecord updateUserRecord(UUID token, RecordRegistration recordRegistration) {
        FinancialRecord existingRecord = financialRecordRepository.findByAppUserId(appService.getUserByToken(token)).orElseThrow(() -> new RuntimeException("Record not found for user with token: " + token));

        if (existingRecord.getAmount() != recordRegistration.getAmount()) {
            existingRecord.setAmount(recordRegistration.getAmount());

        }
        if (existingRecord.getType() != recordRegistration.getType()) {
            existingRecord.setType(recordRegistration.getType());
        }
        if (!existingRecord.getCategory().equals(recordRegistration.getCategory())) {
            existingRecord.setCategory(recordRegistration.getCategory());
        }

        if (!existingRecord.getDescription().equals(recordRegistration.getDescription())) {
            existingRecord.setDescription(recordRegistration.getDescription());
        }
        return financialRecordRepository.save(existingRecord);
    }

    public FinancialRecord deleteUserRecord(UUID token) {
        FinancialRecord existingRecord = financialRecordRepository.findById(appService.getUserByToken(token).getId()).orElseThrow(() -> new RuntimeException("Record not found for user with token: " + token));
        financialRecordRepository.delete(existingRecord);
        return existingRecord;
    }

}
