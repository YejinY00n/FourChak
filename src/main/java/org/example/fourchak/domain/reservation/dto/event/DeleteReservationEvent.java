package org.example.fourchak.domain.reservation.dto.event;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.fourchak.domain.reservation.entity.Reservation;

@Getter
@AllArgsConstructor
public class DeleteReservationEvent {

    private final Long reservationId;
    private final int peopleNumber;
    private final LocalDateTime reservationTime;
    private final Long storeId;
    private final Long userId;

    public static DeleteReservationEvent from(final Reservation reservation) {
        return new DeleteReservationEvent(reservation.getId(), reservation.getPeopleNumber(),
            reservation.getReservationTime(), reservation.getStore().getId(),
            reservation.getUser().getId());
    }
}
