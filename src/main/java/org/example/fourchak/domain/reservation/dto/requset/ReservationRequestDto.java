package org.example.fourchak.domain.reservation.dto.requset;


import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReservationRequestDto {

    private int peopleNumber;

    private LocalDateTime reservationTime;

    public ReservationRequestDto(int peopleNumber, LocalDateTime reservationTime) {
        this.peopleNumber = peopleNumber;
        this.reservationTime = reservationTime;
    }
}
