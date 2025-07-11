package com.demo.demo.api;

import com.demo.demo.dto.AccountRequest;
import com.demo.demo.dto.AccountResponse;
import com.demo.demo.dto.LoginRequest;
import com.demo.demo.entity.Account;
import com.demo.demo.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus; // Import HttpStatus
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping; // Import GetMapping
import org.springframework.web.bind.annotation.PathVariable; // Import PathVariable
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
    public ResponseEntity register(@RequestBody AccountRequest account){
        Account newAccount = authenticationService.register(account);
        // Có thể kiểm tra nếu đăng ký thất bại và trả về lỗi
        if (newAccount == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed"); // Ví dụ xử lý lỗi
        }
        return ResponseEntity.ok(newAccount);
    }
    @PostMapping("/api/login")
    public ResponseEntity login(@RequestBody LoginRequest loginRequest){
        AccountResponse account = authenticationService.login(loginRequest);
        return ResponseEntity.ok(account);
    }

    // --- API mới: Get Account by ID ---
    @GetMapping("/api/account/{id}")
    public ResponseEntity<?> getAccountById(@PathVariable("id") long id) {
        // Gọi service để lấy account
        Account account = authenticationService.getAccountById(id);

        // Kiểm tra nếu kh
        // ông tìm thấy account
        if (account == null) {
            // Trả về 404 Not Found
            return ResponseEntity.notFound().build();
        }

        // Trả về 200 OK và thông tin account
        return ResponseEntity.ok(account);
    }
    // ------------------------------------

}