package org.example.fourchak.common;

import java.util.function.Consumer;
import org.springframework.util.StringUtils;

// 수정할 때 null 값이 들어오면 기존 값 유지
public class UpdateUtils {

    public static void updateString(String value, Consumer<String> setter) {
        if(StringUtils.hasText(value)) {
            setter.accept(value);
        }
    }

    public static void updateLong(Long value, Consumer<Long> setter) {
        if(value != null) {
            setter.accept(value);
        }
    }
}
