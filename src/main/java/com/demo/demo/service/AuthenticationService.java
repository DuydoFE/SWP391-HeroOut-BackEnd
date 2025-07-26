package com.demo.demo.service;

import com.demo.demo.dto.AccountRequest;
import com.demo.demo.dto.AccountResponse;
import com.demo.demo.dto.LoginRequest;
import com.demo.demo.entity.Account;
import com.demo.demo.entity.Consultant;
import com.demo.demo.enums.AccountStatus;
import com.demo.demo.enums.Role;
import com.demo.demo.exception.exceptions.AuthenticationException;
import com.demo.demo.repository.AuthenticationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    AuthenticationRepository authenticationRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    TokenService tokenService;

    @Autowired
    EmailService emailService;

    public Account register(AccountRequest accountRequest) {

        // Kiểm tra xem email đã được đăng ký trước đó chưa
        if (authenticationRepository.findAccountByEmail(accountRequest.getEmail()) != null) {
            // Nếu email đã tồn tại, ném ra một lỗi để API có thể bắt được
            throw new RuntimeException("Tài khoản đã tồn tại");
        }

        Account account = toEntity(accountRequest);

        if (account.getRole() == Role.CONSULTANT) {
            Consultant consultant = new Consultant();
            consultant.setAccount(account);
            account.getConsultants().add(consultant);
        }

        account.setPassword(passwordEncoder.encode(account.getPassword()));
        Account newAccount = authenticationRepository.save(account);
        emailService.sendRegistrationConfirmation(newAccount.getEmail(), newAccount.getName());
        return newAccount;
    }

    public Account getCurrentAccount() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return authenticationRepository.findAccountByEmail(email);
    }

    public static Account toEntity(AccountRequest request) {
        Account account = new Account();
        account.setEmail(request.getEmail());
        account.setPhone(request.getPhone());
        account.setPassword(request.getPassword());
        account.setName(request.getName());
        account.setAddress(request.getAddress());
        account.setAvatar(request.getAvatar());
        account.setDateOfBirth(request.getDateOfBirth());
        account.setGender(request.getGender());
        account.setRole(request.getRole());
        return account;
    }

    public AccountResponse login(LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
            ));

            Account account = authenticationRepository.findAccountByEmail(loginRequest.getEmail());

            if (account != null && account.getStatus() == AccountStatus.INACTIVE) {
                throw new AuthenticationException("tài khoản tạm thời bị vô hiệu hóa");
            }

            if (account == null) {
                throw new AuthenticationException("Lỗi nội bộ: Không tìm thấy tài khoản sau khi xác thực.");
            }

            AccountResponse accountResponse = modelMapper.map(account, AccountResponse.class);
            String token = tokenService.generateToken(account);
            accountResponse.setToken(token);
            return accountResponse;

        } catch (Exception e) {
            throw new AuthenticationException("Email hoặc mật khẩu không chính xác.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = authenticationRepository.findAccountByEmail(email);
        if (account == null) {
            throw new UsernameNotFoundException("Không tìm thấy người dùng với email: " + email);
        }
        return account;
    }

    public Account getAccountById(long id) {
        return authenticationRepository.findById(id).orElse(null);
    }


    public void requestPasswordReset(String email) {
        Account account = authenticationRepository.findAccountByEmail(email);

        if (account == null) {
            System.out.println("Yêu cầu reset mật khẩu cho email không tồn tại: " + email);
            return;
        }

        String otpCode = generateOtp();
        account.setPasswordResetToken(otpCode);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, 15); // Mã OTP có hiệu lực 15 phút
        account.setTokenExpiryDate(cal.getTime());

        authenticationRepository.save(account);

        // Gọi EmailService để gửi mã OTP đi
        emailService.sendPasswordResetEmail(account.getEmail(), account.getName(), otpCode);
    }


    public void resetPassword(String token, String newPassword) {

        Account account = authenticationRepository.findByPasswordResetToken(token);

        if (account == null) {
            throw new AuthenticationException("Mã xác thực không hợp lệ hoặc đã được sử dụng.");
        }

        if (account.getTokenExpiryDate().before(new Date())) {
            // Dọn dẹp token đã hết hạn
            account.setPasswordResetToken(null);
            account.setTokenExpiryDate(null);
            authenticationRepository.save(account);
            throw new AuthenticationException("Mã xác thực đã hết hạn.");
        }

        account.setPassword(passwordEncoder.encode(newPassword));

        // Dọn dẹp token sau khi sử dụng thành công
        account.setPasswordResetToken(null);
        account.setTokenExpiryDate(null);
        authenticationRepository.save(account);
    }


    private String generateOtp() {
        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
}