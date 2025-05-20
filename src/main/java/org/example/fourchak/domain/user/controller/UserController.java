package org.example.fourchak.domain.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.fourchak.common.ResponseMessage;
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

    // email로 유저 정보 찾기
    @GetMapping
    public ResponseEntity<ResponseMessage<UserInfoResponse>> getMyInfo(
        @AuthenticationPrincipal CustomUserPrincipal userPrincipal,
        @Valid @RequestBody UserPasswordRequest passwordRequest
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
            ResponseMessage.success("프로필 조회를 하였습니다.",
                userService.getUserInfo(userPrincipal.getUsername(), passwordRequest))
        );
    }

    // 인덱싱(id)로 유저 정보 찾기 - 최적화,  mysql에서는 기본적으로 B+ tree 구조로 인덱싱 되어 있음
    @GetMapping("/indexing")
    public ResponseEntity<ResponseMessage<UserInfoResponse>> getMyInfoFindIndex(
        @AuthenticationPrincipal CustomUserPrincipal userPrincipal,
        @Valid @RequestBody UserPasswordRequest passwordRequest
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
            ResponseMessage.success("프로필 조회를 하였습니다.",
                userService.getUserInfoFindIndex(userPrincipal.getId(), passwordRequest))
        );
    }


    // 유저이름, 전화번호 수정
    @PutMapping
    public ResponseEntity<ResponseMessage<UserInfoResponse>> putMyInfo(
        @AuthenticationPrincipal CustomUserPrincipal userPrincipal,
        @Valid @RequestBody UsernameAndPhoneRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
            ResponseMessage.success("프로필 수정이 완료되었습니다.",
                userService.putUsernameAndPhone(userPrincipal.getUsername(), request))
        );
    }

    // 패스워드 변경
    @PatchMapping
    public ResponseEntity<ResponseMessage<String>> patchMyPassword(
        @AuthenticationPrincipal CustomUserPrincipal userPrincipal,
        @Valid @RequestBody NewPasswordRequest newPasswordRequest
    ) {
        userService.patchUserPassword(userPrincipal.getUsername(),
            newPasswordRequest);
        return ResponseEntity.ok(ResponseMessage.success("비밀번호 수정이 완료되었습니다."));
    }

    // 회원탈퇴
    @DeleteMapping
    public ResponseEntity<ResponseMessage<String>> deleteMyInfo(
        @AuthenticationPrincipal CustomUserPrincipal userPrincipal,
        @Valid @RequestBody UserPasswordRequest passwordRequest
    ) {
        userService.deleteUserInfo(userPrincipal.getUsername(), passwordRequest);
        return ResponseEntity.ok(ResponseMessage.success("회원탈퇴를 완료 하였습니다."));
    }
}
