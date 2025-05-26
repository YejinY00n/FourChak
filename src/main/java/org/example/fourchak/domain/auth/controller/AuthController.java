package org.example.fourchak.domain.auth.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.fourchak.common.ResponseMessage;
import org.example.fourchak.domain.auth.dto.request.SigninRequest;
import org.example.fourchak.domain.auth.dto.request.SignupRequest;
import org.example.fourchak.domain.auth.dto.response.SigninResponse;
import org.example.fourchak.domain.auth.dto.response.SignupResponse;
import org.example.fourchak.domain.auth.service.AuthService;
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
    public ResponseEntity<ResponseMessage<SignupResponse>> signup(
        @Valid @RequestBody SignupRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            ResponseMessage.success(HttpStatus.CREATED, "회원등록이 완료 되었습니다.",
                authService.signup(request))
        );
    }

    // 로그인 - 토큰발행
    @PostMapping("/signin")
    public ResponseEntity<ResponseMessage<SigninResponse>> signin(
        @Valid @RequestBody SigninRequest request
    ) {
        return ResponseEntity.ok(
            ResponseMessage.success("로그인이 완료 되었습니다.", authService.signin(request))
        );
    }
}
