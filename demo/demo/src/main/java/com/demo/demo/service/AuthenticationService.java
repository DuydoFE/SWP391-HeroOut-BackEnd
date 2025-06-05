package com.demo.demo.service;


import com.demo.demo.dto.AccountResponse;
import com.demo.demo.dto.LoginRequest;
import com.demo.demo.entity.Account;
import com.demo.demo.exception.exceptions.AuthenticationException;
import com.demo.demo.repository.AuthenticationRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLOutput;

@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    AuthenticationRepository authenticationRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;//giup check dang nhap

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    TokenService tokenService;

    public Account register (Account account){
        account.password = passwordEncoder.encode(account.getPassword());
       Account newAccount= authenticationRepository.save(account);
        return newAccount;
    }

    public AccountResponse login(LoginRequest loginRequest){

        try{
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        ));
    }catch (Exception e){

            System.out.println("Thong tin dang nhap sai");

            throw new AuthenticationException("Invalid mail or password");
        }
        Account account =authenticationRepository.findAccountByEmail(loginRequest.getEmail());
        AccountResponse accoutResponse = modelMapper.map(account, AccountResponse.class);
        String token = tokenService.generateToken(account);
        accoutResponse.setToken(token);
        return accoutResponse;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        return authenticationRepository.findAccountByEmail(email);
    }
}
