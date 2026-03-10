package com.example.identityfamily.core.domain.user;


import com.example.identityfamily.application.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private JwtService jwtService;
    private AuthenticationManager authenticationManager;


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

    @Override
    public String singUp(UserRequest userRequest) {
        UserEntity userEntity = UserMapper.mapToEntity(userRequest);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userRepository.save(userEntity);
        return getToken(userEntity);
    }

    private String getToken(UserEntity userEntity) {
        HashMap<String, Object> claims = new HashMap<>();
        String role = "ROLE_" + userEntity.getRole();
        claims.put("role", List.of(role));
        claims.put("permissions", List.of("READ"));
        return jwtService.buildJwt(claims,userEntity);
    }
}
