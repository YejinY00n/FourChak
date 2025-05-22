package org.example.fourchak.common.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode implements ErrorCode {

    // 400 Bad Request
    JWT_TOKEN_REQUIRED(HttpStatus.BAD_REQUEST, "JWT 토큰이 필요합니다."),
    INVALID_JWT_TOKEN(HttpStatus.BAD_REQUEST, "잘못된 JWT 토큰입니다."),
    UNSUPPORTED_JWT_TOKEN(HttpStatus.BAD_REQUEST, "지원되지 않는 JWT 토큰입니다."),
    INVALID_USER_ROLE(HttpStatus.BAD_REQUEST, "잘못된 역할입니다. USER과 OWNER 둘 중 하나를 선택해 주세요"),
    ALREADY_DELETED_USER(HttpStatus.BAD_REQUEST, "이미 탈퇴한 회원입니다."),
    DUPLICATE_PASSWORD_CHANGE(HttpStatus.BAD_REQUEST, "동일한 비밀번호로는 변경할 수 없습니다."),
    VALID_ERROR(HttpStatus.BAD_REQUEST, "Validation 이 유효하지 않습니다"),
    MISMATCH_COUPON_STORE(HttpStatus.BAD_REQUEST, "해당 가게의 쿠폰이 아닙니다"),
    ALREADY_USED_COUPON(HttpStatus.BAD_REQUEST, "이미 사용된 쿠폰입니다"),
    LOCK_EXCEPTION(HttpStatus.BAD_REQUEST, "요청 시간이 초과되었습니다."),

    // 401 Unauthorized = 인증이 안될 때
    INVALID_JWT_SIGNATURE(HttpStatus.UNAUTHORIZED, "유효하지 않는 JWT 서명입니다."),
    EXPIRED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 JWT 토큰입니다."),
    MISS_MATCH_PASSWORD(HttpStatus.UNAUTHORIZED, "잘못된 비밀번호 입니다."),

    // 403 Forbidden = 권한이 없을 때
    NO_OWNER_AUTHORITY(HttpStatus.FORBIDDEN, "사장의 권한이 없습니다."),
    UNAUTHORIZED_STORE_ACCESS(HttpStatus.UNAUTHORIZED, "사용자가 소유한 가게가 아닙니다."),
    UNAUTHORIZED_USERCOUPON_ACCESS(HttpStatus.UNAUTHORIZED, "사용자가 소유한 쿠폰이 아닙니다."),

    // 404 Not Found
    CANT_FIND_DATA(HttpStatus.NOT_FOUND, "해당 데이터를 찾을 수 없습니다."),
    NOT_FOUND_TOKEN(HttpStatus.NOT_FOUND, "해당 토큰을 찾을 수 없습니다."),
    NOT_FOUND_USERNAME(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."),
    NOT_FOUND_EMAIL(HttpStatus.NOT_FOUND, "해당 이메일을 찾을 수 없습니다."),
    NOT_FOUND_STORE(HttpStatus.NOT_FOUND, "해당 가게를 찾을 수 없습니다."),
    NOT_FOUND_COUPON(HttpStatus.NOT_FOUND, "해당 쿠폰을 찾을 수 없습니다."),
    NOT_FOUND_USERCOUPON(HttpStatus.NOT_FOUND, "해당 발급받은 쿠폰을 찾을 수 없습니다."),

    // 409 Conflict = 서버와 충돌, 데이터가 이미 존재할때(400 보다 명확함)
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 등록된 회원입니다."),

    // 500 Server Error
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류 혹은 예기치 못한 예외가 발생했습니다."),

    ACCESS_DENIED_ERROR(HttpStatus.FORBIDDEN, "권한이 없습니다."),
    NO_SEAT_AVAILABLE(HttpStatus.BAD_REQUEST, "남아있는 좌석수가 부족합니다.");

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public int getErrorCode() {
        return httpStatus.value();
    }
}
