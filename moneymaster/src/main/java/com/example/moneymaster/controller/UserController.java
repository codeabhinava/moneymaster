package com.example.moneymaster.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import static org.springframework.http.ResponseEntity.ok;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.moneymaster.model.AppRegistration;
import com.example.moneymaster.model.AppUser;
import com.example.moneymaster.service.AppService;
import com.example.moneymaster.service.ConfirmationService;

@RestController
@RequestMapping("/moneymaster/user")
public class UserController {

    private final AppService appService;
    private final ConfirmationService confirmationService;

    public UserController(AppService appService, ConfirmationService confirmationService) {
        this.appService = appService;
        this.confirmationService = confirmationService;
    }

    @PostMapping("/addUser")
    public ResponseEntity<AppUser> addNewUser(@RequestBody AppRegistration appRegistration) {
        return ResponseEntity.ok(appService.addNewUser(appRegistration));
    }

    @GetMapping("/confirm")
    public ResponseEntity<String> confirmUser(@RequestParam UUID token) {
        return ok(confirmationService.confirmToken(token));
    }
}
