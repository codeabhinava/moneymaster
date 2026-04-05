package com.example.moneymaster.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.moneymaster.model.ConfirmationToken;
import com.example.moneymaster.repository.AppUserRepository;
import com.example.moneymaster.repository.ConfirmationTokenRepository;

@Service
public class ConfirmationService {

    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final AppUserRepository appUserRepository;

    public ConfirmationService(ConfirmationTokenRepository confirmationTokenRepository, AppUserRepository appUserRepository) {
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.appUserRepository = appUserRepository;
    }

    public String getEmailByToken(UUID token) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(token).orElseThrow(() -> new IllegalStateException("Token not found"));
        if (confirmationToken != null) {
            return confirmationToken.getAppUser().getEmail();
        }
        return null;
    }

    @Transactional
    public String confirmToken(UUID token) {

        ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(token).orElseThrow(() -> new IllegalStateException("Token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            return "Already confirmed";
        }
        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            return "Token expired";
        }
        confirmationTokenRepository.updateConfirmedAt(
                token, LocalDateTime.now());
        appUserRepository.enableAppUser(confirmationToken.getAppUser().getEmail());
        return "Token confirmed successfully";

    }
}
