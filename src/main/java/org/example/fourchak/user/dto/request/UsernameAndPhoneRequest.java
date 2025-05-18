package org.example.fourchak.user.dto.request;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UsernameAndPhoneRequest {

    private String username;

    private String phone;
}
