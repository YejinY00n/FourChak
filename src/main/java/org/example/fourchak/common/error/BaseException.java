package org.example.fourchak.common.error;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    ExceptionCode errorCode;

    public BaseException(ExceptionCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}

