package org.example.fourchak.domain.coupon;

import jakarta.persistence.EntityManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.example.fourchak.domain.coupon.entity.Coupon;
import org.example.fourchak.domain.coupon.repository.CouponRepository;
import org.example.fourchak.domain.coupon.service.CouponService;
import org.example.fourchak.domain.store.entity.Store;
import org.example.fourchak.domain.store.repository.StoreRepository;
import org.example.fourchak.domain.user.entity.User;
import org.example.fourchak.domain.user.enums.UserRole;
import org.example.fourchak.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
public class DummyUserGenerator {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    EntityManager em;

    @Autowired
    private CouponService couponService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private CouponRepository couponRepository;

    static List<User> dummyUsers;
    static User owner;
    static Store store;
    static Coupon coupon;
    static int COUPON_COUNT = 1000;

    @Test
    @DisplayName("1만건 더미 테스트 데이터 생성")
    void dummyUserCreate() {
        userRepository.save(
            new User("eee@email.com", "kim",
                "01012345678", "password", UserRole.OWNER.toString()));
        String INSERT_SQL =
            "INSERT INTO users (created_at, modified_at, email, username, phone, password, user_role, is_deleted)"
                +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        int BATCH_SIZE = 1000;
        int TOTAL = COUPON_COUNT * 10;

        // 더미 유저 생성
        for (int i = 0; i < TOTAL / BATCH_SIZE; i++) {
            System.out.println("NOW BATCH : " + i);
            jdbcTemplate.batchUpdate(INSERT_SQL, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
                    ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                    ps.setString(3, UUID.randomUUID() + "@email.com");
                    ps.setString(4, "name" + UUID.randomUUID());
                    ps.setString(5, "01012345678");
                    ps.setString(6, "pw");
                    ps.setString(7, UserRole.USER.name());
                    ps.setBoolean(8, false);
                }

                @Override
                public int getBatchSize() {
                    return BATCH_SIZE;
                }
            });
        }
    }
}
