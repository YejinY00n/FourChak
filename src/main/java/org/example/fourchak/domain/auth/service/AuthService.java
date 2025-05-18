package org.example.fourchak.domain.auth.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.fourchak.common.error.CustomRuntimeException;
import org.example.fourchak.common.error.ExceptionCode;
import org.example.fourchak.config.jwt.JwtUtil;
import org.example.fourchak.config.security.CustomPasswordEncoder;
import org.example.fourchak.domain.auth.dto.request.SigninRequest;
import org.example.fourchak.domain.auth.dto.request.SignupRequest;
import org.example.fourchak.domain.auth.dto.response.SigninResponse;
import org.example.fourchak.domain.auth.dto.response.SignupResponse;
import org.example.fourchak.domain.user.entity.User;
import org.example.fourchak.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final CustomPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // 회원가입
    @Transactional
    public SignupResponse signup(SignupRequest request) {

        // email 중복 방지
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomRuntimeException(ExceptionCode.EMAIL_ALREADY_EXISTS);
        }

        // 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // 등록
        User user = new User(
            request.getEmail(),
            request.getUsername(),
            request.getPhone(),
            encodedPassword,
            request.getUserRole()
        );

        User savedUser = userRepository.save(user);

        return new SignupResponse(
            savedUser.getId(),
            savedUser.getEmail(),
            savedUser.getUsername(),
            savedUser.getPhone(),
            savedUser.getUserRole().toString(),
            savedUser.getCreatedAt()
        );
    }

    // 로그인
    @Transactional
    public SigninResponse signin(@Valid SigninRequest request) {

        // email 확인, 해당 유저정보 가져오기
        User userInfo = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new CustomRuntimeException(ExceptionCode.NOT_FOUND_EMAIL));

        // 비밀번호 확인
        if (!passwordEncoder.matches(request.getPassword(), userInfo.getPassword())) {
            throw new CustomRuntimeException(ExceptionCode.MISS_MATCH_PASSWORD);
        }

        // 토큰 생성
        String bearerToken = jwtUtil.createToken(userInfo.getId(), userInfo.getEmail(),
            userInfo.getUserRole());

        return new SigninResponse(bearerToken);
    }
}
