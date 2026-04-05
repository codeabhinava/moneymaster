package com.example.moneymaster.common;

import java.util.UUID;

public record Email(
        String email,
        UUID confirmationToken
        ) {

}
