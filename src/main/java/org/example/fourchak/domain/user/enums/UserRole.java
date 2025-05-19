package org.example.fourchak.domain.user.enums;


import java.util.Arrays;
import org.example.fourchak.common.error.CustomRuntimeException;
import org.example.fourchak.common.error.ExceptionCode;

public enum UserRole {
    USER, OWNER;

    public static UserRole of(String role) {
        return Arrays.stream(UserRole.values())
            .filter(r -> r.name().equalsIgnoreCase(role))
            .findFirst()
            .orElseThrow(() -> new CustomRuntimeException(ExceptionCode.INVALID_USER_ROLE));
    }
}
