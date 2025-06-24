package com.demo.demo.service;

import com.demo.demo.dto.AccountRequest;
import com.demo.demo.dto.AccountResponse;
import com.demo.demo.dto.EmailDetail;
import com.demo.demo.dto.LoginRequest;
import com.demo.demo.entity.Account;
import com.demo.demo.entity.Consultant;
import com.demo.demo.enums.Role;
import com.demo.demo.enums.AccountStatus; // Có thể cần import này nếu entity Account có status
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

    public Account getCurrentAccount(){
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
    public AccountResponse login(LoginRequest loginRequest){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
            ));
        }catch (Exception e){

            System.out.println("Thông tin đăng nhập ko chính xác");

            throw new AuthenticationException("Invalid username or password");
        }

        Account account = authenticationRepository.findAccountByEmail(loginRequest.getEmail());


        AccountResponse accountResponse = modelMapper.map(account, AccountResponse.class);



        String token = tokenService.generateToken(account);
        accountResponse.setToken(token);
        return accountResponse;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return authenticationRepository.findAccountByEmail(email);
    }

    public Account getAccountById(long id) {

        Optional<Account> accountOptional = authenticationRepository.findById(id);


        return accountOptional.orElse(null);
    }



    // ----------------------------------------------
}