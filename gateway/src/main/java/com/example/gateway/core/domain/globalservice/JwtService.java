package com.example.gateway.core.domain.globalservice;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    // ==================== PUBLIC API ====================

    /**
     * Generate JWT token with extra claims
     */
    public String buildJwt(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .claims(extraClaims)                                    // ✅ Changed from setClaims()
                .subject(userDetails.getUsername())                     // ✅ Changed from setSubject()
                .issuedAt(new Date(System.currentTimeMillis()))         // ✅ Changed from setIssuedAt()
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration)) // ✅ Changed from setExpiration()
                .signWith(getSignInKey(), Jwts.SIG.HS256)              // ✅ SignatureAlgorithm → Jwts.SIG
                .compact();
    }

    /**
     * Generate JWT token without extra claims (convenience overload)
     */
    public String buildJwt(UserDetails userDetails) {
        return buildJwt(Map.of(), userDetails);
    }

    /**
     * Validate token against user details - extracts claims ONCE
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final Claims claims = extractAllClaims(token);
        return userDetails.getUsername().equals(claims.getSubject())
                && !claims.getExpiration().before(new Date());
    }

    /**
     * Extract username from token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Check if token is expired
     */
    public boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    /**
     * Extract a specific claim using a resolver function (DRY & flexible)
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(token));
    }

    // ==================== PRIVATE HELPERS ====================

    /**
     * Parse and extract all claims from token - central point for JWT parsing
     * Updated for JJWT 0.12.6 API
     */
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSignInKey())      // ✅ Changed from setSigningKey()
                    .build()
                    .parseSignedClaims(token)         // ✅ Changed from parseClaimsJws()
                    .getPayload();                    // ✅ Changed from getBody()
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidJwtException("Invalid or expired JWT token", e);
        }
    }

    /**
     * Decode and create signing key from base64 secret
     * Returns SecretKey (compatible with JJWT 0.12+)
     */
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);          // ✅ Returns SecretKey, not Key
    }

    // ==================== CUSTOM EXCEPTION ====================

    /**
     * Runtime exception for JWT-related errors - propagates cleanly through reactive streams
     */
    public static class InvalidJwtException extends RuntimeException {
        public InvalidJwtException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}