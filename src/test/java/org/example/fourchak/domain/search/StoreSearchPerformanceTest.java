package org.example.fourchak.domain.search;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.DoubleSummaryStatistics;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class StoreSearchPerformanceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;
    private static final String SEARCH_KEYWORD = "떡볶이";
    private static final int TEST_ITERATIONS = 20;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port;
    }

    @Test
    @DisplayName("v1 API (캐시 미사용) 성능 테스트")
    void testV1ApiPerformance() {
        // Given
        String url = baseUrl + "/api/v1/stores/search?keyword=" + SEARCH_KEYWORD;
        List<Long> responseTimes = new ArrayList<>();

        // When - 20회 반복 호출
        for (int i = 0; i < TEST_ITERATIONS; i++) {
            long startTime = System.currentTimeMillis();
            
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            
            long endTime = System.currentTimeMillis();
            long responseTime = endTime - startTime;
            responseTimes.add(responseTime);

            // Then - 응답 검증
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            
            System.out.println("v1 API 호출 " + (i + 1) + "회: " + responseTime + "ms");
        }

        // 통계 출력
        printStatistics("v1 API (캐시 미사용)", responseTimes);
    }

    @Test
    @DisplayName("v2 API (캐시 사용) 성능 테스트")
    void testV2ApiPerformance() {
        // Given
        String url = baseUrl + "/api/v2/stores/search?keyword=" + SEARCH_KEYWORD;
        List<Long> responseTimes = new ArrayList<>();

        // When - 20회 반복 호출
        for (int i = 0; i < TEST_ITERATIONS; i++) {
            long startTime = System.currentTimeMillis();
            
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            
            long endTime = System.currentTimeMillis();
            long responseTime = endTime - startTime;
            responseTimes.add(responseTime);

            // Then - 응답 검증
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            
            System.out.println("v2 API 호출 " + (i + 1) + "회: " + responseTime + "ms");
        }

        // 통계 출력
        printStatistics("v2 API (캐시 사용)", responseTimes);
    }

    @Test
    @DisplayName("v1 vs v2 API 성능 비교 테스트")
    void compareApiPerformance() {
        // Given
        String v1Url = baseUrl + "/api/v1/stores/search?keyword=" + SEARCH_KEYWORD;
        String v2Url = baseUrl + "/api/v2/stores/search?keyword=" + SEARCH_KEYWORD;
        
        List<Long> v1ResponseTimes = new ArrayList<>();
        List<Long> v2ResponseTimes = new ArrayList<>();

        // When - 각각 20회 호출
        System.out.println("=== v1 API 테스트 시작 ===");
        for (int i = 0; i < TEST_ITERATIONS; i++) {
            long responseTime = measureResponseTime(v1Url);
            v1ResponseTimes.add(responseTime);
            System.out.println("v1 API 호출 " + (i + 1) + "회: " + responseTime + "ms");
        }

        System.out.println("\n=== v2 API 테스트 시작 ===");
        for (int i = 0; i < TEST_ITERATIONS; i++) {
            long responseTime = measureResponseTime(v2Url);
            v2ResponseTimes.add(responseTime);
            System.out.println("v2 API 호출 " + (i + 1) + "회: " + responseTime + "ms");
        }

        // Then - 통계 비교
        System.out.println("\n=== 성능 비교 결과 ===");
        printStatistics("v1 API (캐시 미사용)", v1ResponseTimes);
        printStatistics("v2 API (캐시 사용)", v2ResponseTimes);
        
        double v1Average = v1ResponseTimes.stream().mapToLong(Long::longValue).average().orElse(0);
        double v2Average = v2ResponseTimes.stream().mapToLong(Long::longValue).average().orElse(0);
        double improvement = ((v1Average - v2Average) / v1Average) * 100;
        
        System.out.println("\n성능 향상률: " + String.format("%.2f", improvement) + "%");
        System.out.println("평균 응답 시간 단축: " + String.format("%.2f", v1Average - v2Average) + "ms");
    }

    private long measureResponseTime(String url) {
        long startTime = System.currentTimeMillis();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        long endTime = System.currentTimeMillis();
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        return endTime - startTime;
    }

    private void printStatistics(String apiName, List<Long> responseTimes) {
        DoubleSummaryStatistics stats = responseTimes.stream()
                .mapToDouble(Long::doubleValue)
                .summaryStatistics();

        // 첫 번째 요청 제외한 통계 (워밍업 제외)
        DoubleSummaryStatistics statsWithoutFirst = responseTimes.stream()
                .skip(1)
                .mapToDouble(Long::doubleValue)
                .summaryStatistics();

        System.out.println("\n=== " + apiName + " 통계 ===");
        System.out.println("전체 평균: " + String.format("%.2f", stats.getAverage()) + "ms");
        System.out.println("최소값: " + stats.getMin() + "ms");
        System.out.println("최대값: " + stats.getMax() + "ms");
        System.out.println("첫 요청 제외 평균: " + String.format("%.2f", statsWithoutFirst.getAverage()) + "ms");
        
        // 중간값 계산
        List<Long> sorted = responseTimes.stream().sorted().collect(Collectors.toList());
        long median = sorted.get(sorted.size() / 2);
        System.out.println("중간값: " + median + "ms");
    }
}
