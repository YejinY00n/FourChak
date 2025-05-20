package org.example.fourchak.domain.user.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UsernameAndPhoneRequest {

    private String username;

    private String phone;

    @NotBlank
    private String password;
}
