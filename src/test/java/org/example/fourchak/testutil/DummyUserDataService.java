package org.example.fourchak.testutil;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Profile("test")
@RequiredArgsConstructor
public class DummyUserDataService {

    private final JdbcTemplate jdbcTemplate;

    private final int BATCH_SIZE = 1000;

    private final PasswordEncoder passwordEncoder;

    public void insertDummyUsers(int totalCount) {
        String sql = "INSERT INTO users (email, username, phone, password, user_role) VALUES (?, ?, ?, ?, ?)";

        List<Object[]> batchArgs = new ArrayList<>();

        for (int i = 1; i <= totalCount; i++) {
            String email = "email" + i;
            String username = "user" + i;
            String phone = "0100000" + String.format("%04d", i);
            String password = passwordEncoder.encode("1234"); // 실제 서비스라면 인코딩된 비밀번호 넣어야 함
            String userRole = "USER";

            batchArgs.add(new Object[]{email, username, phone, password, userRole});

            if (i % BATCH_SIZE == 0) {
                jdbcTemplate.batchUpdate(sql, batchArgs);
                batchArgs.clear();
                System.out.println("Inserted batch up to user #" + i);
            }
        }

        // 남은 레코드 처리
        if (!batchArgs.isEmpty()) {
            jdbcTemplate.batchUpdate(sql, batchArgs);
            System.out.println("Inserted remaining users.");
        }

        System.out.println("100만 개 유저 더미 데이터 삽입 완료!");
    }
}