package org.example.fourchak.config.cache;

import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class CacheConfig {

    @Bean
    @Primary // 여러 개의 CacheManager가 존재할 때, 기본값으로 사용할 것을 명시
    public CacheManager cacheManager() {
        // ConcurrentMapCacheManager 메모리 내에서 캐시를 관리하는 기본 구현체, 애플리케이션이 꺼지면 캐시도 사라지는 메모리 기반 캐시
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();
        // 사용할 캐시 이름들을 등록, @Cacheable(value = "storeSearch") 처럼 사용
        cacheManager.setCacheNames(java.util.Arrays.asList("storeSearch", "popularKeywords"));
        return cacheManager;
    }

}
