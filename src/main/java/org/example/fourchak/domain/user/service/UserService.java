package org.example.fourchak.domain.user.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.fourchak.common.UpdateUtils;
import org.example.fourchak.common.error.CustomRuntimeException;
import org.example.fourchak.common.error.ExceptionCode;
import org.example.fourchak.domain.user.dto.request.NewPasswordRequest;
import org.example.fourchak.domain.user.dto.request.UserPasswordRequest;
import org.example.fourchak.domain.user.dto.request.UsernameAndPhoneRequest;
import org.example.fourchak.domain.user.dto.response.UserInfoResponse;
import org.example.fourchak.domain.user.entity.User;
import org.example.fourchak.domain.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserInfoResponse getUserInfo(String email, UserPasswordRequest passwordRequest) {

        User userInfo = findInfoAndCheckPassword(email, passwordRequest.getPassword());

        return new UserInfoResponse(
            userInfo.getId(),
            userInfo.getEmail(),
            userInfo.getUsername(),
            userInfo.getPhone(),
            userInfo.getUserRole().toString(),
            userInfo.getCreatedAt(),
            userInfo.getModifiedAt()
        );
    }

    public UserInfoResponse getUserInfoFindIndex(Long id,
        @Valid UserPasswordRequest passwordRequest) {

        User userInfo = userRepository.findUserByOnwerIdOrElseThrow(id);

        if (!passwordEncoder.matches(passwordRequest.toString(), userInfo.getPassword())) {
            throw new CustomRuntimeException(ExceptionCode.MISS_MATCH_PASSWORD);
        }

        return new UserInfoResponse(
            userInfo.getId(),
            userInfo.getEmail(),
            userInfo.getUsername(),
            userInfo.getPhone(),
            userInfo.getUserRole().toString(),
            userInfo.getCreatedAt(),
            userInfo.getModifiedAt()
        );
    }

    @Transactional
    public UserInfoResponse putUsernameAndPhone(String email, UsernameAndPhoneRequest request) {

        User userInfo = findInfoAndCheckPassword(email, request.getPassword());

        // 수정 수행 - null 값이 들어오면 변화 x
        UpdateUtils.updateString(request.getUsername(), userInfo::setUsername);
        UpdateUtils.updateString(request.getPhone(), userInfo::setPhone);

        return new UserInfoResponse(userInfo);
    }

    @Transactional
    public void patchUserPassword(String email, NewPasswordRequest newPasswordRequest) {

        User userInfo = findInfoAndCheckPassword(email, newPasswordRequest.getPassword());

        String encodedPassword = passwordEncoder.encode(newPasswordRequest.getNewPassword());

        // 동일한 비밀번호 시
        if (passwordEncoder.matches(encodedPassword, newPasswordRequest.getPassword())) {
            throw new CustomRuntimeException(ExceptionCode.DUPLICATE_PASSWORD_CHANGE);
        }

        userInfo.setPassword(encodedPassword);
    }

    @Transactional
    public void deleteUserInfo(String email, UserPasswordRequest passwordRequest) {

        User userInfo = findInfoAndCheckPassword(email, passwordRequest.getPassword());

        /* Hard delete
        userRepository.delete(userInfo);
         */

        // soft delete
        userInfo.isDelete();
    }

    // 토큰으로 정보 가져오고 패스워드를 체크하는 메소드
    private User findInfoAndCheckPassword(String email, String passwordRequest) {
        // email 확인, 해당 유저정보 가져오기
        User userInfo = userRepository.findByEmail(email)
            .orElseThrow(() -> new CustomRuntimeException(ExceptionCode.NOT_FOUND_EMAIL));

        // 비밀번호 확인
        if (!passwordEncoder.matches(passwordRequest, userInfo.getPassword())) {
            throw new CustomRuntimeException(ExceptionCode.MISS_MATCH_PASSWORD);
        }

        return userInfo;
    }


}
