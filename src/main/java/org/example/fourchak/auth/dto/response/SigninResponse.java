package org.example.fourchak.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SigninResponse {

    private final String bearerToken;

}
