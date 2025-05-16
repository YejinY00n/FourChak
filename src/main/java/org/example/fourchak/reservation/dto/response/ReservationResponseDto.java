package org.example.fourchak.reservation.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.fourchak.reservation.entity.Reservation;

@Getter
@AllArgsConstructor
public class ReservationResponseDto {

    private String userName;
    private String storeName;
    private int peopleNumber;
    private LocalDateTime reserveTime;
    private LocalDateTime createdAt;

    public static ReservationResponseDto from(Reservation reservation) {
        return new ReservationResponseDto(
            reservation.getUser().getUsername(),
            reservation.getStore().getStoreName(),
            reservation.getPeopleNumber(),
            reservation.getReservationTime(),
            reservation.getCreatedAt()
        );
    }

}
