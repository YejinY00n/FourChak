package org.example.fourchak.testutil;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("local")
@RequiredArgsConstructor
public class DummyDataInitializer {

    private final DummyUserDataService dummyUserDataService;

    // 사용 할려면 아래 주석만 제거 하세요.
    //@PostConstruct
    public void init() {
        dummyUserDataService.insertDummyUsers(1_000); // 100만개
    }
}
