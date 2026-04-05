package com.example.moneymaster.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

import com.example.moneymaster.service.AppService;

@Component
@EnableWebSecurity
public class SecurityConfig {

    private final AppService appUserService;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(AppService appUserService, PasswordEncoder passwordEncoder) {
        this.appUserService = appUserService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                .requestMatchers("/moneymaster/**", "/users/**", "/restaurants/**", "/css/**", "/js/**", "/images/**").permitAll()
                .anyRequest().authenticated()
                )
                .formLogin(form -> form
                .defaultSuccessUrl("/quickbite/home", true)
                )
                .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
                );
        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(
                appUserService);

        authProvider.setPasswordEncoder(passwordEncoder.bcryptpasswordEncoder());
        return authProvider;
    }

}
