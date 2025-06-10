package com.demo.demo.api;

import com.demo.demo.dto.AccountResponse;
import com.demo.demo.dto.LoginRequest;
import com.demo.demo.entity.Account;
import com.demo.demo.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
public class AuthenticationAPI {

    @Autowired
    AuthenticationService authenticationService;

    // api > service > repository

    @PostMapping("/api/register")
    public ResponseEntity register(@RequestBody Account account){
        Account newAccount = authenticationService.register(account);
        return ResponseEntity.ok(newAccount);
    }

    @PostMapping("/api/login")
    public ResponseEntity login(@RequestBody LoginRequest loginRequest){

        AccountResponse account = authenticationService.login(loginRequest);

        return ResponseEntity.ok(account);
    }
}
