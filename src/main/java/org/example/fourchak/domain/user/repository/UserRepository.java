package org.example.fourchak.domain.user.repository;

import java.util.Optional;
import org.example.fourchak.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserById(Long userId);

    default User findUserByOnwerIdOrElseThrow(Long onwerId) {
        User user = findUserById(onwerId).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 id입니다."));

        if (!user.getUserRole().equals(UserRole.OWNER)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 권한은 OWNER만 가능합니다.");
        }
        return user;
    }

}
