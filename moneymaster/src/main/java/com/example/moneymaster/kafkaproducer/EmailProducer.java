package com.example.moneymaster.kafkaproducer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.moneymaster.common.Email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailProducer {

    private final KafkaTemplate<String, Email> kafkaTemplate;
    private static final String TOPIC = "email-topic";

    public void sendEmailConfirmation(Email email) {
        String message = "Email: " + email.email() + ", Confirmation Token: " + email.confirmationToken();
        kafkaTemplate.send(TOPIC, email);
        log.info("Sent email confirmation for Email:{} with Token:{} " + message);
    }

}
