package org.example.fourchak.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserPasswordRequest {

    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
        message = "비밀번호는 소영문,대영문자와 숫자를 포함한 8자 이상이어야 합니다.")
    private String password;
}
