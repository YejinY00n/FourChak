package org.example.fourchak.domain.waiting.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetMyWaitingResponse {

    private final int waitingNumber;
    private final LocalDateTime reservationTime;
    private final int peopleNum;
    private final Long storeId;
}
