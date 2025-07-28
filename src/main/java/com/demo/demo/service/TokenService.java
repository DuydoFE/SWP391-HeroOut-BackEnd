package com.demo.demo.service;

import com.demo.demo.entity.Account; // Đảm bảo Account class có method getId() và getRole()
import com.demo.demo.repository.AuthenticationRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class TokenService {

    @Autowired
    AuthenticationRepository authenticationRepository;

    private final String SECRET_KEY = "4bb6d1dfbafb64a681139d1586b6f1160d18159afd57c8c79136d7490630407c";

    private SecretKey getSigninKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Account account) {

        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null for token generation");
        }



        String token =
                // create object of JWT
                Jwts.builder()
                        //subject of token (thường là thông tin nhận dạng chính)
                        .subject(account.getEmail()) // Giữ lại email làm subject
                        .claim("id", account.getId())
                        .claim("role", account.getRole())

                        // time Create Token
                        .issuedAt(new Date(System.currentTimeMillis()))

                        // Time exprire of Token (24 giờ sau thời điểm tạo)
                        .expiration(new Date(System.currentTimeMillis()+24*60*60*1000))

                        // Ký token bằng khóa bí mật
                        .signWith(getSigninKey())

                        // Kết thúc quá trình xây dựng và tạo chuỗi token
                        .compact();

        return token;
    }


    public Claims extractAllClaims(String token) {
        return  Jwts.parser()
                .verifyWith(getSigninKey()) // Xác minh chữ ký
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    public Account extractAccount (String token){
        String email = extractClaim(token,Claims::getSubject); // Lấy email từ subject
        // Tìm Account trong database dựa vào email
        return authenticationRepository.findAccountByEmail(email);
    }


    public Long extractAccountId(String token) {

        return extractClaim(token, claims -> claims.get("id", Long.class));
    }


    public String extractAccountRole(String token) {

        return extractClaim(token, claims -> claims.get("role", String.class));
    }



    public boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }


    public Date extractExpiration(String token){
        return extractClaim(token,Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        Claims claims = extractAllClaims(token);
        return  claimsResolver.apply(claims);
    }


}