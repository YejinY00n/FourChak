package org.example.fourchak.domain.waiting.dto.request;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RegisterWaitingRequest {

    private final LocalDateTime reservationTime;
    private final int peopleNum;
    private final Long userId;
}
