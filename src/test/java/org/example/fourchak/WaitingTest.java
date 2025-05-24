package org.example.fourchak;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import org.example.fourchak.domain.store.entity.Store;
import org.example.fourchak.domain.store.repository.StoreRepository;
import org.example.fourchak.domain.user.entity.User;
import org.example.fourchak.domain.user.repository.UserRepository;
import org.example.fourchak.domain.waiting.dto.request.RegisterWaitingRequest;
import org.example.fourchak.domain.waiting.entity.Waiting;
import org.example.fourchak.domain.waiting.repository.WaitingRepository;
import org.example.fourchak.domain.waiting.service.WaitingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class WaitingServiceConcurrencyTest {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
        .withDatabaseName("fourchak")
        .withUsername("root")
        .withPassword("password");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
        registry.add("spring.datasource.driver-class-name", mysql::getDriverClassName);
    }

    @Autowired
    private WaitingService waitingService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private WaitingRepository waitingRepository;

    private User user;
    private Store store;
    private LocalDateTime reservationTime;

    @BeforeEach
    void setUp() {
        user = userRepository.save(
            new User("test@email.com", "Test", "Password1234", "010-0000-0000", "USER"));
        store = storeRepository.save(new Store("store", "02-0000-0000", 5, user));
        reservationTime = LocalDateTime.now().plusDays(7).withHour(12).withMinute(0).withSecond(0)
            .withNano(0);
    }

    @Test
    void 동시에_여러명이_대기등록을_하면_waitingNum이_정확하게_붙는다() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CyclicBarrier barrier = new CyclicBarrier(threadCount);

        AtomicInteger waitingNum = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {

            final int index = i;
            executorService.submit(() -> {
                try {
                    barrier.await();
                    RegisterWaitingRequest request = new RegisterWaitingRequest(
                        reservationTime, 2, user.getId()
                    );
                    waitingService.register(store.getId(), request);
                } catch (Exception e) {

                } finally {
                    failCount.incrementAndGet();
                    System.out.println("대기 실패" + failCount.get());
                }
            });
        }

        List<Waiting> result = waitingRepository.findAll();
        assertEquals(threadCount, result.size());

        List<Integer> waitingNums = result.stream()
            .map(Waiting::getWaitingNumber)
            .sorted()
            .toList();

        // 1부터 n까지 waitingNum이 순차적으로 있어야 한다
        for (int i = 0; i < threadCount; i++) {
            assertEquals(i + 1, waitingNums.get(i));
        }
    }
}
