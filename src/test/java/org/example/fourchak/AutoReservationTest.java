package org.example.fourchak;

import org.example.fourchak.domain.store.repository.StoreRepository;
import org.example.fourchak.domain.waiting.facade.AutomaticReservationFacade;
import org.example.fourchak.domain.waiting.repository.WaitingRepository;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;

@DisplayName("예약 대기가 있는 상태에서 다른사람이 예약 취소를 하면 자동으로 등록한다")
public class AutoReservationTest {

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private org.example.fourchak.reservation.repository.ReservationRepository reservationRepository;

    @Mock
    private WaitingRepository waitingRepository;

    @InjectMocks
    private AutomaticReservationFacade automaticReservationFacade;
}