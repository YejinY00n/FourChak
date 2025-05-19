package org.example.fourchak.domain.waiting.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RegisterWaitingResponse {

    private final Long waitingId;
    private final int waitingNum;
    private final LocalDateTime reservationTime;
    private final int peopleNum;
    private final Long userId;
    private final Long storeId;
    private final LocalDateTime createAt;
}
