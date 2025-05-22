package org.example.fourchak.domain.waiting.facade;

import lombok.RequiredArgsConstructor;
import org.example.fourchak.domain.reservation.dto.event.DeleteReservationEvent;
import org.example.fourchak.domain.reservation.service.ReservationService;
import org.example.fourchak.domain.waiting.service.WaitingService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class AutomaticReservationFacade {

    ReservationService reservationService;
    WaitingService waitingService;

    //store 에서 총 자리수 가져와서 연산하기
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void doIt(DeleteReservationEvent event) {
        System.out.println("이벤트 수신");
        int availableSeat = reservationService.countReservationPeopleAtTime(event.getStoreId(),
            event.getReservationTime());

        waitingService.autoDeleteWaiting(event, availableSeat);

        reservationService.justSave(event.getPeopleNumber(), event.getReservationTime(),
            event.getStoreId(), event.getUserId());
    }
}
