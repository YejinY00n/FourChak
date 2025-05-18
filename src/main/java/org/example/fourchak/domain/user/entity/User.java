package org.example.fourchak.domain.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.fourchak.common.SoftDelete;
import org.example.fourchak.domain.user.enums.UserRole;
import org.hibernate.annotations.Where;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "users")
@Where(clause = "is_deleted = false")
public class User extends SoftDelete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String username;

    private String phone;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    public User(String email, String username, String phone, String password, String userRole) {
        this.email = email;
        this.username = username;
        this.phone = phone;
        this.password = password;
        this.userRole = UserRole.of(userRole);
    }
}
