package com.example.moneymaster.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RecordRegistration {

    private final double amount;
    private final TransactionType type;
    private final String category;
    private final String description;
}
