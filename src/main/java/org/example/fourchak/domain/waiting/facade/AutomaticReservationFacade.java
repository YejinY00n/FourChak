package org.example.fourchak.domain.waiting.facade;

import lombok.RequiredArgsConstructor;
import org.example.fourchak.domain.reservation.dto.event.DeleteReservationEvent;
import org.example.fourchak.domain.reservation.service.ReservationService;
import org.example.fourchak.domain.waiting.service.WaitingService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AutomaticReservationFacade {

    ReservationService reservationService;
    WaitingService waitingService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void doIt(DeleteReservationEvent event) {
        int availableSeat = reservationService.countReservationPeopleAtTime(event.getStoreId(),
            event.getReservationTime());

        waitingService.autoDeleteWaiting(event, availableSeat);
        reservationService.justSave(event.getPeopleNumber(), event.getReservationTime(),
            event.getStoreId(), event.getUserId());
    }
}
