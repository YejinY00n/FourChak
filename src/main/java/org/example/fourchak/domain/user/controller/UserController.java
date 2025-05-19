package org.example.fourchak.domain.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.fourchak.domain.user.dto.request.NewPasswordRequest;
import org.example.fourchak.domain.user.dto.request.UserPasswordRequest;
import org.example.fourchak.domain.user.dto.request.UsernameAndPhoneRequest;
import org.example.fourchak.domain.user.dto.response.UserInfoResponse;
import org.example.fourchak.domain.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
        @RequestHeader("Authorization") String bearerToken,
        @Valid @RequestBody UserPasswordRequest passwordRequest
    ) {
        String token = bearerToken.replace("Bearer", "");

        return new ResponseEntity<>(userService.getUserInfoByToken(token, passwordRequest),
            HttpStatus.OK);
    }

    // 유저이름, 전화번호 수정
    @PutMapping
    public ResponseEntity<UserInfoResponse> putMyInfo(
        @RequestHeader("Authorization") String bearerToken,
        @Valid @RequestBody UsernameAndPhoneRequest request,
        @Valid @RequestBody UserPasswordRequest passwordRequest
    ) {
        String token = bearerToken.replace("Bearer", "");

        return new ResponseEntity<>(
            userService.putUsernameAndPhone(token, request, passwordRequest), HttpStatus.OK);
    }

    // 패스워드 변경
    @PatchMapping
    public ResponseEntity<String> patchMyPassword(
        @RequestHeader("Authorization") String bearerToken,
        @Valid @RequestBody UserPasswordRequest passwordRequest,
        @Valid @RequestBody NewPasswordRequest newPasswordRequest
    ) {
        String token = bearerToken.replace("Bearer", "");

        userService.patchUserPassword(token, passwordRequest, newPasswordRequest);
        return new ResponseEntity<>("비밀번호 수정이 완료되었습니다.", HttpStatus.OK);
    }

    // 회원탈퇴
    @DeleteMapping
    public ResponseEntity<String> deleteMyInfo(
        @RequestHeader("Authorization") String bearerToken,
        @Valid @RequestBody UserPasswordRequest passwordRequest
    ) {
        String token = bearerToken.replace("Bearer", "");

        userService.deleteUserInfo(token, passwordRequest);
        return new ResponseEntity<>("회원탈퇴를 완료 하였습니다.", HttpStatus.OK);
    }
}
