package org.example.fourchak.testutil;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
@RequiredArgsConstructor
public class DummyDataInitializer {

    private final DummyUserDataService dummyUserDataService;

    @PostConstruct
    public void init() {
        dummyUserDataService.insertDummyUsers(1_000_000); // 100만개
    }
}
