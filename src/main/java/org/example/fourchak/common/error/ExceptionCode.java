package org.example.fourchak.common.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException.Forbidden;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode implements ErrorCode {

    // 400 Bad Request
    JWT_TOKEN_REQUIRED(HttpStatus.BAD_REQUEST, "JWT 토큰이 필요합니다."),
    INVALID_JWT_TOKEN(HttpStatus.BAD_REQUEST, "잘못된 JWT 토큰입니다."),
    UNSUPPORTED_JWT_TOKEN(HttpStatus.BAD_REQUEST, "지원되지 않는 JWT 토큰입니다."),

    // 401 Unauthorized = 인증이 안될 때
    INVALID_JWT_SIGNATURE(HttpStatus.UNAUTHORIZED, "유효하지 않는 JWT 서명입니다."),
    EXPIRED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 JWT 토큰입니다."),
    MISS_MATCH_PASSWORD(HttpStatus.UNAUTHORIZED, "잘못된 비밀번호 입니다."),

    // 403 Forbidden = 권한이 없을 때
    NO_ADMIN_AUTHORITY(HttpStatus.FORBIDDEN, "관리자 권한이 없습니다."),

    // 404 Not Found
    CANT_FIND_DATA(HttpStatus.NOT_FOUND, "해당 데이터를 찾을 수 없습니다."),
    NOT_FOUND_TOKEN(HttpStatus.NOT_FOUND, "해당 토큰을 찾을 수 없습니다."),
    NOT_FOUND_USERNAME(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."),
    NOT_FOUND_EMAIL(HttpStatus.NOT_FOUND, "해당 이메일을 찾을 수 없습니다."),

    // 409 Conflict = 서버와 충돌, 데이터가 이미 존재할때(400 보다 명확함)
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),

    // 500 Server Error
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.")
    ;


    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
