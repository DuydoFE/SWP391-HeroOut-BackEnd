package com.demo.demo.api;

import com.demo.demo.dto.UpdateAccountRequest;
import com.demo.demo.entity.Account;
import com.demo.demo.service.AccountService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@SecurityRequirement(name = "api")
public class AccountAPI {

    @Autowired
    private AccountService accountService;

    @PutMapping("/update")
    public Account updateAccount(@RequestParam long id, @RequestBody UpdateAccountRequest request) {
        return accountService.updateAccount(id, request);
    }

    @GetMapping
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }
}