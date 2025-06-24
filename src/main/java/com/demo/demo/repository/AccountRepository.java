package com.demo.demo.repository;

import com.demo.demo.entity.Account;
import com.demo.demo.enums.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmailAndStatus(String email, AccountStatus status);
    List<Account> findAllByStatus(AccountStatus status);
}