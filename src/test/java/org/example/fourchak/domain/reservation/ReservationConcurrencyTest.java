package org.example.fourchak.domain.reservation;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.example.fourchak.config.security.CustomUserPrincipal;
import org.example.fourchak.domain.reservation.dto.requset.ReservationRequestDto;
import org.example.fourchak.domain.reservation.service.ReservationService;
import org.example.fourchak.domain.store.entity.Store;
import org.example.fourchak.domain.store.repository.StoreRepository;
import org.example.fourchak.domain.user.entity.User;
import org.example.fourchak.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
public class ReservationConcurrencyTest {

    @Autowired
    private ReservationService reservationService;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private UserRepository userRepository;

    private Store store;
    private User user;

    @BeforeEach
    void setUp() {
        // 테스트용 유저, 가게 생성
        user = userRepository.save(
            new User("test@email.com", "Test", "Password1234", "010-0000-0000", "USER"));
        store = storeRepository.save(new Store("store", "02-0000-0000", 5, user)); // 좌석 5개
    }

    @Test
    void 동시_예약_테스트() throws Exception {
        // Given: 테스트용 유저, 가게, 쓰레드 환경 준비
        int threadCount = 30;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CyclicBarrier barrier = new CyclicBarrier(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);
        List<Exception> exceptions = Collections.synchronizedList(new ArrayList<>());

        // When: 30개의 스레드가 동시에 예약 시도
        for (int i = 0; i < threadCount; i++) {
            final int idx = i;
            executorService.submit(() -> {
                try {
                    barrier.await(); // 모든 스레드가 준비될 때까지 대기
                    ReservationRequestDto dto = new ReservationRequestDto(1,
                        LocalDateTime.now().plusDays(1));
                    CustomUserPrincipal principal = new CustomUserPrincipal(user);

                    reservationService.saveReservation(principal, dto, store.getId());

                    successCount.incrementAndGet();
                    System.out.println("예약 성공: " + idx);
                } catch (Exception e) {
                    failCount.incrementAndGet();
                    exceptions.add(e);
                    System.out.println("예약 실패: " + idx + " - " + e.getMessage());
                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);

        // Then: 예약은 최대 5개까지만 성공해야 하고, 모든 시도는 처리되어야 한다
        System.out.println("성공한 예약 수: " + successCount.get());
        System.out.println("실패한 예약 수: " + failCount.get());
        System.out.println("총 시도한 예약 수: " + (successCount.get() + failCount.get()));

        assertThat(successCount.get())
            .as("좌석 수 초과")
            .isLessThanOrEqualTo(5);

        assertThat(successCount.get() + failCount.get())
            .as("모든 요청 처리")
            .isEqualTo(threadCount);
    }
}

