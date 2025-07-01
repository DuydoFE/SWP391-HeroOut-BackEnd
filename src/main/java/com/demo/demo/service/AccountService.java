package com.demo.demo.service;

import com.demo.demo.dto.UpdateAccountRequest;
import com.demo.demo.entity.Account;
import com.demo.demo.enums.AccountStatus;
import com.demo.demo.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account updateAccount(long id, UpdateAccountRequest request) {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isEmpty()) {
            throw new RuntimeException("Account not found with id: " + id);
        }

        Account account = optionalAccount.get();
        account.setName(request.getName());
        account.setPhone(request.getPhone());
        account.setAddress(request.getAddress());
        account.setAvatar(request.getAvatar());
        account.setDateOfBirth(request.getDateOfBirth());
        account.setGender(request.getGender()); // Added setter for gender

        return accountRepository.save(account);
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public void updateAccountStatus(Long id, AccountStatus status) {
        Account acc = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        acc.setStatus(status);
        accountRepository.save(acc);
    }
}