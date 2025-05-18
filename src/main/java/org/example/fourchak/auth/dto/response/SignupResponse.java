package org.example.fourchak.auth.dto.response;


import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class SignupResponse {

    private Long id;

    private String email;

    private String username;

    private String phone;

    private String password;

    private String userRole;

    private LocalDateTime createdAt;

    public SignupResponse(Long id, String email, String username, String phone, String password,
        String userRole, LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.phone = phone;
        this.password = password;
        this.userRole = userRole;
        this.createdAt = createdAt;
    }
}
