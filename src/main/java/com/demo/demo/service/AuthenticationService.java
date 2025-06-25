package com.demo.demo.service;

import com.demo.demo.dto.AccountRequest;
import com.demo.demo.dto.AccountResponse;
import com.demo.demo.dto.EmailDetail;
import com.demo.demo.dto.LoginRequest;
import com.demo.demo.entity.Account;
import com.demo.demo.entity.Consultant;
import com.demo.demo.enums.Role;
import com.demo.demo.enums.AccountStatus; // Import AccountStatus
import com.demo.demo.exception.exceptions.AuthenticationException; // Sử dụng lại exception này
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

import java.util.Optional;

@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    AuthenticationRepository authenticationRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager; // giúp check đăng nhập

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    TokenService tokenService;

    @Autowired
    EmailService emailService;

    public Account register(AccountRequest accountRequest) {
        // Convert DTO to Account entity
        Account account = toEntity(accountRequest);
      
        account.setPassword(passwordEncoder.encode(account.getPassword()));

        if (account.getRole() == Role.CONSULTANT) {
            Consultant consultant = new Consultant();
            consultant.setAccount(account);
            account.setConsultant(consultant); // ✅ Gán trực tiếp thay vì add vào Set
        }

        // Lưu vào DB
        Account newAccount = authenticationRepository.save(account);

        // Gửi email xác nhận
        emailService.sendRegistrationConfirmation(newAccount.getEmail(), newAccount.getName());

        return newAccount;
    }


    public Account getCurrentAccount(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return authenticationRepository.findAccountByEmail(email);
    }


    // Giả định Account entity có trường `status` kiểu AccountStatus và các trường khác
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

    public AccountResponse login(LoginRequest loginRequest){
        Account account = null; // Khai báo account ở đây để có thể truy cập sau block try

        try {

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
            ));


            account = authenticationRepository.findAccountByEmail(loginRequest.getEmail());


            if (account != null && account.getStatus() == AccountStatus.INACTIVE) {

                System.out.println("Login blocked: Account INACTIVE for email: " + loginRequest.getEmail());
                throw new AuthenticationException("tài khoản tạm thời bị vô hiệu hóa"); // Thông báo lỗi yêu cầu
            }


            if (account == null) {
                System.err.println("Internal error: Authenticated user email not found in repository: " + loginRequest.getEmail());
                throw new AuthenticationException("Login failed due to internal error.");
            }


        }catch (AuthenticationException e){

            System.out.println("Authentication failed: " + e.getMessage());

            throw e;
        } catch (Exception e){

            System.err.println("An unexpected error occurred during login: " + e.getMessage());

            throw new AuthenticationException("An unexpected error occurred during login.");
        }


        AccountResponse accountResponse = modelMapper.map(account, AccountResponse.class);

        String token = tokenService.generateToken(account);
        accountResponse.setToken(token);


        return accountResponse;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Account account = authenticationRepository.findAccountByEmail(email);
        if (account == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        return account;
    }


    public Account getAccountById(long id) {

        Optional<Account> accountOptional = authenticationRepository.findById(id);


        return accountOptional.orElse(null);
    }

    // ----------------------------------------------
}