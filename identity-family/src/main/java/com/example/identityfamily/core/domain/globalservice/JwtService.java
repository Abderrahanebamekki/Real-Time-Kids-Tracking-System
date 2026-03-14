package com.example.identityfamily.core.domain.globalservice;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;


@Service
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private  String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration ;

    public String buildJwt(
            Map<String,Object> claims ,
            UserDetails userDetails
    ){
         return Jwts
                 .builder()
                 .setClaims(claims)
                 .setSubject(userDetails.getUsername())
                 .setIssuedAt(new Date(System.currentTimeMillis()))
                 .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                 .signWith(getSingInkey(),SignatureAlgorithm.HS256)
                 .compact();
    }



    private Key getSingInkey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSingInkey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }
}
