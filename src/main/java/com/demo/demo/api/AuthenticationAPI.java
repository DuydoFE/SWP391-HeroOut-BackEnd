package com.demo.demo.api;

import com.demo.demo.dto.*;
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

import java.util.Map;

@RestController
@CrossOrigin("*")

public class AuthenticationAPI {

    @Autowired
    AuthenticationService authenticationService;

    // api > service > repository

    @PostMapping("/api/register")
    public ResponseEntity<?> register(@RequestBody AccountRequest account) {
        try {
            Account newAccount = authenticationService.register(account);
            // Nếu đăng ký thành công, trả về tài khoản mới và mã 200 OK
            return ResponseEntity.ok(newAccount);
        } catch (RuntimeException e) {
            // Nếu service ném ra lỗi (ví dụ: email đã tồn tại),
            // bắt lỗi đó và trả về thông báo lỗi cùng mã 400 Bad Request
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
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

        if (account == null) {
            // Trả về 404 Not Found
            return ResponseEntity.notFound().build();
        }

        // Trả về 200 OK và thông tin account
        return ResponseEntity.ok(account);
    }
    @PostMapping("/api/forgot-password")
    // Thay đổi Map<String, String> thành ForgotPasswordRequest
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        String email = request.getEmail();

        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body("Email is required.");
        }
        authenticationService.requestPasswordReset(email);

        return ResponseEntity.ok("Nếu tài khoản tồn tại, một mã xác nhận đã được gửi đến email của bạn.");
    }

    // --- API MỚI: ĐẶT LẠI MẬT KHẨU MỚI ---
    @PostMapping("/api/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest passwordResetRequest) {
        try {
            authenticationService.resetPassword(
                    passwordResetRequest.getToken(),
                    passwordResetRequest.getNewPassword()
            );
            return ResponseEntity.ok("Mật khẩu của bạn đã được đặt lại thành công.");
        } catch (Exception e) {
            // Bắt các lỗi (token sai, hết hạn) từ service và trả về cho client
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


}