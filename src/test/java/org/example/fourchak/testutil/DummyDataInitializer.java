package org.example.fourchak.testutil;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
@RequiredArgsConstructor
public class DummyDataInitializer {

    private final DummyUserDataService dummyUserDataService;

    // 사용 할려면 먼저 User 엔티티에서 email에 unique 설정을 false로 바꾸세요
    // 이후 아래 주석('//')만 제거 하세요.
    // @PostConstruct
    public void init() {
        dummyUserDataService.insertDummyUsers(10_000);
    }
}
