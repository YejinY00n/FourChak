package org.example.fourchak.domain.waiting.dto.request;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterWaitingRequest {

    private LocalDateTime reservationTime;
    private int peopleNum;
    private Long userId;

}
