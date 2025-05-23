package org.example.fourchak.domain.user.repository;

import java.util.Optional;
import org.example.fourchak.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
public class UserRepositoryIndexTest {

    @Autowired
    private UserRepository userRepository;

    private final String testEmail = "emailnY8HtX"; //9998 번째
    private final Long testId = 9998L;

    private final int REPEAT_COUNT = 100;

    @BeforeEach
    void checkDummyData() {
        Optional<User> byEmail = userRepository.findByEmail(testEmail);
        Optional<User> byId = userRepository.findById(testId);

        if (byEmail.isEmpty() || byId.isEmpty()) {
            throw new RuntimeException("테스트용 사용자(email100000, id 100000)가 DB에 존재해야 합니다.");
        }
    }

    @Test
    @DisplayName("이메일 기반 조회 성능 측정")
    void testFindByEmailPerformance() {
        long start = System.currentTimeMillis();

        for (int i = 0; i < REPEAT_COUNT; i++) {
            userRepository.findByEmail(testEmail);
        }

        long end = System.currentTimeMillis();
        System.out.println("이메일 조회 평균 시간: " + (end - start) / (double) REPEAT_COUNT + " ms");
    }

    @Test
    @DisplayName("ID 기반 조회 성능 측정")
    void testFindByIdPerformance() {
        long start = System.currentTimeMillis();

        for (int i = 0; i < REPEAT_COUNT; i++) {
            userRepository.findById(testId);
        }

        long end = System.currentTimeMillis();
        System.out.println("ID 조회 평균 시간: " + (end - start) / (double) REPEAT_COUNT + " ms");
    }
}
