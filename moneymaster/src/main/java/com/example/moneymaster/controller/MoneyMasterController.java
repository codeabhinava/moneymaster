package com.example.moneymaster.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.moneymaster.model.FinancialRecord;
import com.example.moneymaster.model.RecordRegistration;
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

}
