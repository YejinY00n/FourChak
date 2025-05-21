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

    private final int BATCH_SIZE = 100;

    private final PasswordEncoder passwordEncoder;

    public void insertDummyUsers(int totalCount) {
        String sql = "INSERT INTO users (email, username, phone, password, user_role,is_deleted) VALUES (?, ?, ?, ?, ?,?)";

        List<Object[]> batchArgs = new ArrayList<>();

        for (int i = 1; i <= totalCount; i++) {
            String email = "email" + i;
            String username = "user" + i;
            String phone = "010-0000-0000";
            String password = passwordEncoder.encode("1234");
            String userRole = "USER";

            batchArgs.add(new Object[]{email, username, phone, password, userRole, false});

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