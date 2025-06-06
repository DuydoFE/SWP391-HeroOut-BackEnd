package com.demo.demo.repository;

import com.demo.demo.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthenticationRepository extends JpaRepository<Account, Long> {

    // findAccountByEmail

    Account findAccountByEmail(String email);
}
