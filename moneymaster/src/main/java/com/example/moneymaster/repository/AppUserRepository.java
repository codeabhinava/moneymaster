package com.example.moneymaster.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.moneymaster.model.AppUser;

@Repository
@Transactional(readOnly = true)
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByUsername(String username);

    @Query("SELECT a FROM AppUser a WHERE a.confirmationToken = ?1")
    AppUser findByToken(UUID token);

    @Modifying
    @Transactional
    @Query("UPDATE AppUser a SET a.isEnabled = true WHERE a.email = ?1")
    int enableAppUser(String emailId);

}
