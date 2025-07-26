// file: src/main/java/com/demo/demo/service/AccountService.java

package com.demo.demo.service;

import com.demo.demo.dto.UpdateAccountRequest;
import com.demo.demo.entity.Account;
import com.demo.demo.enums.AccountStatus;
import com.demo.demo.enums.AgeGroup;
import com.demo.demo.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Cần import Transactional

import java.time.LocalDate;
import java.time.Period;
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
        account.setGender(request.getGender());

        return accountRepository.save(account);
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Transactional // Thêm transactional để tự động quản lý transaction
    public void updateAccountStatus(Long id, AccountStatus status) {
        Account acc = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản với ID: " + id));
        acc.setStatus(status);
        // Với @Transactional, JPA sẽ tự động lưu khi transaction kết thúc.
    }

    // ===== PHƯƠNG THỨC CẬP NHẬT AVATAR ĐÃ SỬA LỖI =====
    /**
     * Tìm một tài khoản bằng ID và cập nhật trường avatar của nó.
     * @param accountId ID của tài khoản cần cập nhật.
     * @param avatarUrl URL mới của avatar (nhận được từ Cloudinary).
     * @throws RuntimeException nếu không tìm thấy tài khoản.
     */
    @Transactional
    public void updateAvatar(Long accountId, String avatarUrl) {
        // Sử dụng orElseThrow để code gọn hơn và ném ra lỗi nếu không tìm thấy
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản với ID: " + accountId));

        // Cập nhật trường avatar
        account.setAvatar(avatarUrl);

        // Gọi save() một cách tường minh để đảm bảo thay đổi được ghi vào cơ sở dữ liệu.
        accountRepository.save(account);

    }


    public static int calculateAge(LocalDate birthDate) {
        if (birthDate == null) return 0;
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    public static AgeGroup getAgeGroup(LocalDate birthDate) {
        int age = calculateAge(birthDate);
        if (age < 12) return AgeGroup.CHILDREN;
        if (age <= 17) return AgeGroup.TEENAGERS;
        if (age <= 25) return AgeGroup.YOUNG_ADULTS;
        if (age <= 59) return AgeGroup.ADULTS;
        return AgeGroup.SENIORS;
    }
}