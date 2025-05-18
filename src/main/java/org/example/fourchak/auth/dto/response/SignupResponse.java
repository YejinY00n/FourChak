package org.example.fourchak.auth.dto.response;


import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignupResponse {

    private Long id;

    private String email;

    private String username;

    private String phone;

    private String userRole;

    private LocalDateTime createdAt;

}
