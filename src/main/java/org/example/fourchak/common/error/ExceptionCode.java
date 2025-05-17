package org.example.fourchak.common.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode implements ErrorCode {

    CANT_FIND_DATA(HttpStatus.NOT_FOUND, "해당 데이터를 찾을 수 없습니다."),
    ACCESS_DENIED_ERROR(HttpStatus.FORBIDDEN, "권한이 없습니다."),
    NO_SEAT_AVAILABLE(HttpStatus.BAD_REQUEST, "남아있는 좌석수가 부족합니다.");

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
