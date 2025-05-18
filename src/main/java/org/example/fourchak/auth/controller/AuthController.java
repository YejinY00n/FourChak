package org.example.fourchak.auth.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.fourchak.auth.dto.request.SigninRequest;
import org.example.fourchak.auth.dto.request.SignupRequest;
import org.example.fourchak.auth.dto.response.SignupResponse;
import org.example.fourchak.auth.dto.response.SigninResponse;
import org.example.fourchak.auth.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(
        @Valid @RequestBody SignupRequest request
    ) {
        return new ResponseEntity<>(authService.signup(request), HttpStatus.OK);
    }

    // 로그인 - 토큰발행
    public ResponseEntity<SigninResponse> signin(
        @Valid @RequestBody SigninRequest request
    ) {
        return new ResponseEntity<>(authService.signin(request),HttpStatus.OK);
    }
}
