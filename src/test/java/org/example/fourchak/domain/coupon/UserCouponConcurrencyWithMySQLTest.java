package org.example.fourchak.domain.coupon;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import org.example.fourchak.domain.coupon.entity.Coupon;
import org.example.fourchak.domain.coupon.repository.CouponRepository;
import org.example.fourchak.domain.coupon.repository.UserCouponRepository;
import org.example.fourchak.domain.coupon.service.UserCouponService;
import org.example.fourchak.domain.reservation.repository.ReservationRepository;
import org.example.fourchak.domain.store.entity.Store;
import org.example.fourchak.domain.store.repository.StoreRepository;
import org.example.fourchak.domain.user.entity.User;
import org.example.fourchak.domain.user.enums.UserRole;
import org.example.fourchak.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;

@SpringBootTest
public class UserCouponConcurrencyWithMySQLTest {

    @Autowired
    private UserCouponService userCouponService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private UserCouponRepository userCouponRepository;
    @Autowired
    private ReservationRepository reservationRepository;

    static List<User> dummyUsers;
    static User owner;
    static Store store;
    static Coupon coupon;
    static Long couponId;
    static int COUPON_COUNT = 1000;
    static int THREAD_COUNT = 1000;

    @BeforeEach
    void setUp() {
        userCouponRepository.deleteAll();
        couponRepository.deleteAll();
        reservationRepository.deleteAll();
        storeRepository.deleteAll();

//        owner = userRepository.findById(1L)
//            .orElseThrow(() -> new BaseException(ExceptionCode.NOT_FOUND_USERNAME));
        owner = userRepository.save(
            new User("eee@email.com", "kim",
                "01012345678", "password", UserRole.OWNER.toString()));
        store = storeRepository.save(
            new Store("버거리 버거", "02123456", 16, owner));
        coupon = couponRepository.save(
            new Coupon(20, COUPON_COUNT));  // 20% 할인 쿠폰
        couponId = coupon.getId();

        dummyUsers = userRepository.findAll(Sort.by("id"));
        dummyUsers.remove(0); // 사장 유저만 제외
    }

    @Test
    @DisplayName("쿠폰 동시 발급 테스트 with MySQL 네임드 락, JDBC")
    void testConcurrentIssue() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        int memberCount = COUPON_COUNT + 200;
        CountDownLatch latch = new CountDownLatch(memberCount);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        Long start = System.currentTimeMillis();
        for (int i = 0; i < memberCount; i++) {
            User user = dummyUsers.get(i);
            executor.submit(() -> {
                try {
                    userCouponService.issueCouponWithNamedLockAndJdbc(user, couponId);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        executor.shutdown();
        Long end = System.currentTimeMillis();

        Coupon updatedCoupon = couponRepository.findById(coupon.getId()).orElseThrow();
        Long userCouponCount = userCouponRepository.count();

        System.out.println("===== [TEST END] =====");
        System.out.println("총 실행 시간: " + (end - start) + " ms");
        System.out.println("남은 쿠폰 수량: " + updatedCoupon.getCount());
        System.out.println("유저 쿠폰 개수: " + userCouponCount);
        System.out.println("성공 횟수: " + successCount);
        System.out.println("실패 횟수: " + failCount);

        assertThat(updatedCoupon.getCount())
            .as("남은 쿠폰 수량이 쿠폰 개수-성공 횟수와 동일")
            .isEqualTo(COUPON_COUNT - successCount.get());
        assertThat(userCouponCount)
            .as("유저 쿠폰은 쿠폰 발급량 이하")
            .isLessThanOrEqualTo(COUPON_COUNT);
    }

    @Test
    @DisplayName("쿠폰 동시 발급 테스트 with MySQL 네임드 락, DataSource")
    void testConcurrentIssueWithDataSource() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        int memberCount = COUPON_COUNT + 200;
        CountDownLatch latch = new CountDownLatch(memberCount);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        for (int i = 0; i < memberCount; i++) {
            User user = dummyUsers.get(i);
            executor.submit(() -> {
                try {
                    userCouponService.issueCouponWithNamedLockAndDS(user, couponId);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        executor.shutdown();
        Coupon updatedCoupon = couponRepository.findById(coupon.getId()).orElseThrow();
        long userCouponCount = userCouponRepository.count();

        System.out.println("[TEST END] 남은 쿠폰 수량: " + updatedCoupon.getCount());
        System.out.println("유저 쿠폰 개수: " + userCouponRepository.count());
        System.out.println("성공 횟수: " + successCount);
        System.out.println("실패 횟수: " + failCount);

        assertThat(updatedCoupon.getCount())
            .as("남은 쿠폰 수량이 쿠폰 개수-성공 횟수와 동일")
            .isEqualTo(COUPON_COUNT - successCount.get());
        assertThat(userCouponCount)
            .as("유저 쿠폰은 쿠폰 발급량 이하")
            .isLessThanOrEqualTo(COUPON_COUNT);
    }

}
