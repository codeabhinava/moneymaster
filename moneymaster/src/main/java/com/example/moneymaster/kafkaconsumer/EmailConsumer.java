package com.example.moneymaster.kafkaconsumer;

import java.util.UUID;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.example.moneymaster.common.Email;
import com.example.moneymaster.email.EmailSender;
import com.example.moneymaster.model.AppUser;
import com.example.moneymaster.service.AppService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EmailConsumer {

    private final AppService userService;
    private final EmailSender emailSender;

    public EmailConsumer(AppService userService, EmailSender emailSender) {
        this.userService = userService;
        this.emailSender = emailSender;
    }

    @KafkaListener(topics = "email-topic", groupId = "email-group")
    public void consumeEmailConfirmation(Email email) {
        log.info("Received email confirmation:{} " + email.confirmationToken());
        UUID uuidToken = email.confirmationToken();

        String link = "http://localhost:8080/moneymaster/user/confirm?token=" + uuidToken;
        AppUser user = userService.getUserByToken(uuidToken);
        emailSender.send(user.getEmail(), emailSender.buildEmail(user.getUsername(), link));

    }

}
