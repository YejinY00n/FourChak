package org.example.fourchak.user.repository;

import org.example.fourchak.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
