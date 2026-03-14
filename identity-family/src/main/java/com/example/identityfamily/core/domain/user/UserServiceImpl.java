package com.example.identityfamily.core.domain.user;


import com.example.identityfamily.core.domain.exception.CodeExpiredException;
import com.example.identityfamily.core.domain.exception.CodeIsNotTrue;
import com.example.identityfamily.core.domain.exception.UserNotFound;
import com.example.identityfamily.core.domain.globalservice.EmailService;
import com.example.identityfamily.core.domain.globalservice.JwtService;
import com.example.identityfamily.core.domain.exception.EmailAlreadyExists;
import com.example.identityfamily.core.domain.globalservice.VerificationCodeGenerator;
import com.example.identityfamily.core.domain.verficationcode.VerificationCodeEntity;
import com.example.identityfamily.core.domain.verficationcode.VerificationCodeRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final VerificationCodeRepository verificationCodeRepository;


    @Override
    public String singIn(UserRequest userRequest) {
        UserEntity userEntity = userRepository.findByUsername(userRequest.getUsername());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userRequest.getUsername(),
                        userRequest.getPassword()
                )
        );

        return getToken(userEntity);

    }

    @SneakyThrows
    @Override
    public Long singUp(UserRequest userRequest) {
        UserEntity userEntity = UserMapper.mapToEntity(userRequest);
        if(userRepository.existsByUsername(userEntity.getUsername())){
            throw new EmailAlreadyExists(userEntity.getUsername());
        }
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userEntity = userRepository.save(userEntity);
        VerificationCodeEntity verificationCode = new VerificationCodeEntity();
        verificationCode.setUser(userEntity);
        verificationCode = verificationCodeRepository.save(verificationCode);
        emailService.sendVerificationEmail(userEntity.getUsername() , verificationCode.getCode());
        return userEntity.getId();
    }

    @Override
    public String verificationCode(Long userId, String code) {

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(UserNotFound::new);

        VerificationCodeEntity verificationCode = verificationCodeRepository
                .findByCodeAndUser(code, user)
                .orElseThrow(CodeIsNotTrue::new);

        if (verificationCode.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new CodeExpiredException();
        }

        return getToken(user);
    }

    @SneakyThrows
    @Override
    public void resendCode(Long user_id) {
        UserEntity user = userRepository.findById(user_id)
                .orElseThrow(UserNotFound::new);

        VerificationCodeEntity verificationCode = new VerificationCodeEntity();
        verificationCode.setUser(user);
        emailService.sendVerificationEmail(user.getUsername() , verificationCode.getCode());
    }

    private String getToken(UserEntity userEntity) {
        HashMap<String, Object> claims = new HashMap<>();
        String role = "ROLE_" + userEntity.getRole();
        claims.put("role", List.of(role));
        return jwtService.buildJwt(claims,userEntity);
    }
}
