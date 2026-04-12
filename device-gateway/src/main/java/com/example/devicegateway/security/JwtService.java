package com.example.devicegateway.security;


import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JWKService jwkService;

    public String generateToken(String deviceId) throws Exception{
        RSAKey rsaKey = jwkService.getRsaKey();
        JWSSigner signer = new RSASSASigner(rsaKey.toPrivateKey());
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject(deviceId)
                .expirationTime(new Date(System.currentTimeMillis() + 15 * 60 * 1000))
                .build();
        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader.Builder(JWSAlgorithm.RS256)
                        .keyID(rsaKey.getKeyID())
                        .build(),
                claims
        );
        signedJWT.sign(signer);

        return signedJWT.serialize();
    }

}
