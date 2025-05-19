package org.example.fourchak.domain.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.fourchak.config.security.CustomUserPrincipal;
import org.example.fourchak.domain.user.dto.request.NewPasswordRequest;
import org.example.fourchak.domain.user.dto.request.UserPasswordRequest;
import org.example.fourchak.domain.user.dto.request.UsernameAndPhoneRequest;
import org.example.fourchak.domain.user.dto.response.UserInfoResponse;
import org.example.fourchak.domain.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 토큰으로 유저 정보 찾기
    @GetMapping
    public ResponseEntity<UserInfoResponse> getMyInfo(
        @AuthenticationPrincipal CustomUserPrincipal userPrincipal,
        @Valid @RequestBody UserPasswordRequest passwordRequest
    ) {
        // userPrincipal.getUsername()에는 사용자 이름 대신 email을 요청함
        return new ResponseEntity<>(
            userService.getUserInfoByToken(userPrincipal.getUsername(), passwordRequest),
            HttpStatus.OK);
    }

    // 유저이름, 전화번호 수정
    @PutMapping
    public ResponseEntity<UserInfoResponse> putMyInfo(
        @AuthenticationPrincipal CustomUserPrincipal userPrincipal,
        @Valid @RequestBody UsernameAndPhoneRequest request
    ) {
        return new ResponseEntity<>(
            userService.putUsernameAndPhone(userPrincipal.getUsername(), request),
            HttpStatus.OK);
    }

    // 패스워드 변경
    @PatchMapping
    public ResponseEntity<String> patchMyPassword(
        @AuthenticationPrincipal CustomUserPrincipal userPrincipal,
        @Valid @RequestBody NewPasswordRequest newPasswordRequest
    ) {
        userService.patchUserPassword(userPrincipal.getUsername(),
            newPasswordRequest);
        return new ResponseEntity<>("비밀번호 수정이 완료되었습니다.", HttpStatus.OK);
    }

    // 회원탈퇴
    @DeleteMapping
    public ResponseEntity<String> deleteMyInfo(
        @AuthenticationPrincipal CustomUserPrincipal userPrincipal,
        @Valid @RequestBody UserPasswordRequest passwordRequest
    ) {
        userService.deleteUserInfo(userPrincipal.getUsername(), passwordRequest);
        return new ResponseEntity<>("회원탈퇴를 완료 하였습니다.", HttpStatus.OK);
    }
}
