package com.example.moneymaster.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.moneymaster.common.Email;
import com.example.moneymaster.kafkaproducer.EmailProducer;
import com.example.moneymaster.model.AppRegistration;
import com.example.moneymaster.model.AppRole;
import com.example.moneymaster.model.AppUser;
import com.example.moneymaster.model.ConfirmationToken;
import com.example.moneymaster.repository.AppUserRepository;
import com.example.moneymaster.repository.ConfirmationTokenRepository;
import com.example.moneymaster.security.PasswordEncoder;

@Service
public class AppService implements UserDetailsService {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final EmailProducer emailProducer;

    public AppService(AppUserRepository userRepository, PasswordEncoder passwordEncoder, ConfirmationTokenRepository confirmationTokenRepository, EmailProducer emailProducer) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.emailProducer = emailProducer;
    }

    public AppUser addNewUser(AppRegistration appRegistration) {
        UUID token = UUID.randomUUID();
        AppUser newUser = new AppUser(appRegistration.getUserName(),
                passwordEncoder.bcryptpasswordEncoder().encode(appRegistration.getPassword()),
                appRegistration.getEmail(),
                AppRole.USER,
                token);
        userRepository.save(newUser);
        confirmationTokenRepository.save(new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), newUser));

        emailProducer.sendEmailConfirmation(new Email(appRegistration.getEmail(), token));
        return newUser;
    }

    public AppUser getUserByToken(UUID token) {
        return userRepository.findByToken(token);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return user;
    }

}
