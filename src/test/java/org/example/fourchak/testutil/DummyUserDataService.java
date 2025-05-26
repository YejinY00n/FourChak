package org.example.fourchak.testutil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.example.fourchak.config.TestSecurityConfig;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Import(TestSecurityConfig.class)
@Service
@Profile("test")
@RequiredArgsConstructor
public class DummyUserDataService {

    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;

    private final int BATCH_SIZE = 100;
    private final Random random = new Random();

    public void insertDummyUsers(int totalCount) {
        String sql = "INSERT INTO users (email, username, phone, password, user_role, is_deleted) VALUES (?, ?, ?, ?, ?,?)";

        List<Object[]> batchArgs = new ArrayList<>();
        Set<String> usedEmails = new HashSet<>();

        for (int i = 1; i <= totalCount; i++) {
            String email;
            do {
                email = "email" + generateRandomString(6);
            } while (usedEmails.contains(email) || emailExists(email));

            usedEmails.add(email);

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

        System.out.println("1만 개 유저 더미 데이터 삽입 완료!");
    }

    private boolean emailExists(String email) {
        String query = "SELECT COUNT(*) FROM users WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(query, Integer.class, email);
        return count != null && count > 0;
    }

    private String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }

        return sb.toString();
    }
}
