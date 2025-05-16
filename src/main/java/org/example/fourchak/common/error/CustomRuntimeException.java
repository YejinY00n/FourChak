package org.example.fourchak.common.error;

import lombok.Getter;

@Getter
public class CustomRuntimeException extends RuntimeException {

    private final ExceptionCode exceptionCode;

    public CustomRuntimeException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }
}
