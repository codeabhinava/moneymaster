package com.example.moneymaster.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.moneymaster.model.FinancialRecord;
import com.example.moneymaster.model.RecordRegistration;
import com.example.moneymaster.model.TransactionType;
import com.example.moneymaster.service.RecordService;

@RestController
@RequestMapping("/moneymaster/finance")
public class MoneyMasterController {

    private final RecordService recordService;

    public MoneyMasterController(RecordService recordService) {
        this.recordService = recordService;
    }

    @PostMapping("/{token}/addRecord")
    public ResponseEntity<FinancialRecord> addRecord(@PathVariable UUID token, @RequestBody RecordRegistration recordRegistration) {
        FinancialRecord savedRecord = recordService.saveRecord(token, recordRegistration);
        return ResponseEntity.ok(savedRecord);
    }

    @GetMapping("{token}/viewRecord")
    public ResponseEntity<List<FinancialRecord>> viewRecord(@PathVariable UUID token) {
        List<FinancialRecord> record = recordService.viewUserRecord(token);
        return ResponseEntity.ok(record);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("{token}/updateRecord/{id}")
    public ResponseEntity<FinancialRecord> updateRecord(@PathVariable UUID token, @RequestBody RecordRegistration recordRegistration, @PathVariable Long id) {
        FinancialRecord updatedRecord = recordService.updateUserRecord(token, recordRegistration, id);
        return ResponseEntity.ok(updatedRecord);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{token}/deleteRecord/{id}")
    public ResponseEntity<FinancialRecord> deleteRecord(@PathVariable UUID token, @PathVariable Long id) {
        FinancialRecord deletedRecord = recordService.deleteUserRecord(token, id);
        return ResponseEntity.ok(deletedRecord);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('ANALYST')")
    @GetMapping("{token}/viewRecordByCategory/{category}")
    public ResponseEntity<List<FinancialRecord>> viewRecordByCategory(@PathVariable UUID token, @PathVariable String category) {
        List<FinancialRecord> record = recordService.viewUserRecordByCategory(token, category);
        return ResponseEntity.ok(record);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('ANALYST')")
    @GetMapping("{token}/viewRecordByDate/{startDate}/{endDate}")
    public ResponseEntity<List<FinancialRecord>> viewRecordByDate(@PathVariable UUID token, @PathVariable String startDate, @PathVariable String endDate) {
        List<FinancialRecord> record = recordService.viewUserRecordByDate(token, startDate, endDate);
        return ResponseEntity.ok(record);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('ANALYST')")
    @GetMapping("{token}/viewRecordByCategoryAndDate/{category}/{startDate}/{endDate}")
    public ResponseEntity<List<FinancialRecord>> viewRecordByCategoryAndDate(@PathVariable UUID token, @PathVariable String category, @PathVariable String startDate, @PathVariable String endDate) {
        List<FinancialRecord> record = recordService.viewUserRecordByCategoryAndDate(token, category, startDate, endDate);
        return ResponseEntity.ok(record);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('ANALYST')")
    @GetMapping("{token}/viewRecordByCategoryAndTypeAndDate/{category}/{type}/{startDate}/{endDate}")
    public ResponseEntity<List<FinancialRecord>> viewRecordByCategoryAndTypeAndDate(@PathVariable UUID token, @PathVariable String category, @PathVariable String type, @PathVariable String startDate, @PathVariable String endDate) {
        List<FinancialRecord> record = recordService.viewUserRecordByCategoryAndTypeAndDate(token, category, type, startDate, endDate);
        return ResponseEntity.ok(record);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('ANALYST')")
    @GetMapping("{token}/viewRecordByDateAndId/{startDate}/{id}")
    public ResponseEntity<List<FinancialRecord>> viewRecordByDateAndId(@PathVariable UUID token, @PathVariable String startDate, @PathVariable Long id) {
        List<FinancialRecord> record = recordService.viewUserRecordByDateAndId(token, id);
        return ResponseEntity.ok(record);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('ANALYST')")
    @GetMapping("{token}/viewTotalExpense")
    public ResponseEntity<Double> viewTotalExpense(@PathVariable UUID token) {
        Double totalExpense = recordService.viewTotalExpense(token);
        return ResponseEntity.ok(totalExpense);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('ANALYST')")
    @GetMapping("{token}/viewTotalIncome")
    public ResponseEntity<Double> viewTotalIncome(@PathVariable UUID token) {
        Double totalIncome = recordService.viewTotalIncome(token);
        return ResponseEntity.ok(totalIncome);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('ANALYST')")
    @GetMapping("{token}/viewTotalByTypeAndCategory/{type}/{category}")
    public ResponseEntity<Double> viewTotalByTypeAndCategory(@PathVariable UUID token, @PathVariable String type, @PathVariable String category) {
        Double total = recordService.viewTotalByTypeAndCategory(token, type, category);
        return ResponseEntity.ok(total);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('ANALYST')")
    @GetMapping("{token}/viewTotalByCategory/{category}")
    public ResponseEntity<Double> viewTotalByCategory(@PathVariable UUID token, @PathVariable String category) {
        Double total = recordService.viewTotalByCategory(token, category);
        return ResponseEntity.ok(total);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('ANALYST')")
    @GetMapping("{token}/viewTotalByType/{type}")
    public ResponseEntity<Double> viewTotalByType(@PathVariable UUID token, @PathVariable TransactionType type) {
        Double total = recordService.viewTotalByType(token, type);
        return ResponseEntity.ok(total);
    }

}
