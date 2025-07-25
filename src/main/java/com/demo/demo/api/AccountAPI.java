package com.demo.demo.api;

import com.demo.demo.dto.UpdateAccountRequest;
import com.demo.demo.entity.Account;
import com.demo.demo.enums.AccountStatus;
import com.demo.demo.service.AccountService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map; // Cần import thêm Map

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

    @PutMapping("/{id}/status") // Đổi sang dùng PathVariable cho id để nhất quán
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestParam("status") String status) {
        try {
            accountService.updateAccountStatus(id, AccountStatus.valueOf(status.toUpperCase()));
            return ResponseEntity.ok("Cập nhật trạng thái thành công");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Trạng thái không hợp lệ");
        }
    }


    @PutMapping("/{id}/avatar")
    public ResponseEntity<?> updateAvatar(@PathVariable Long id, @RequestBody String avatarUrl) {
        // Kiểm tra xem frontend có gửi URL hay không
        // Khi nhận trực tiếp, chuỗi có thể chứa cả dấu ngoặc kép, cần loại bỏ
        String cleanAvatarUrl = avatarUrl.replace("\"", "");

        if (cleanAvatarUrl == null || cleanAvatarUrl.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("URL của avatar không được để trống.");
        }

        try {
            // Gọi service để thực hiện logic cập nhật
            accountService.updateAvatar(id, cleanAvatarUrl);
            // Trả về response thành công
            return ResponseEntity.ok(Map.of("message", "Cập nhật avatar thành công."));
        } catch (RuntimeException e) { // Bắt lỗi nếu không tìm thấy tài khoản
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}