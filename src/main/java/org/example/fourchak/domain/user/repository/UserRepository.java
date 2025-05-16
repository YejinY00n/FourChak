package org.example.fourchak.domain.user.repository;

import org.example.fourchak.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
