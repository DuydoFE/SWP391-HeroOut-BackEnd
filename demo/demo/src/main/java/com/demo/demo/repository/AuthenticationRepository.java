package com.demo.demo.repository;

import java.util.List;

import com.demo.demo.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthenticationRepository extends JpaRepository<Account, Long> {

    // lay account by email

    Account findAccountByEmail(String email);

}
