package org.example.fourchak.domain.user.dto.response;


import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.fourchak.domain.user.entity.User;

@Getter
@AllArgsConstructor
public class UserInfoResponse {

    private Long id;

    private String email;

    private String username;

    private String phone;

    private String userRole;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    public UserInfoResponse(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.phone = user.getPhone();
        this.userRole = user.getUserRole().name();
        this.createdAt = user.getCreatedAt();
        this.modifiedAt = user.getModifiedAt();
    }
}
