package org.example.fourchak.reservation.service;

import lombok.RequiredArgsConstructor;
import org.example.fourchak.common.error.CustomRuntimeException;
import org.example.fourchak.common.error.ExceptionCode;
import org.example.fourchak.reservation.dto.requset.ReservationRequestDto;
import org.example.fourchak.reservation.dto.response.ReservationResponseDto;
import org.example.fourchak.reservation.entity.Reservation;
import org.example.fourchak.reservation.repository.ReservationRepository;
import org.example.fourchak.store.entity.Store;
import org.example.fourchak.store.repository.StoreRepository;
import org.example.fourchak.user.entity.User;
import org.example.fourchak.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public ReservationResponseDto saveReservation(CustomUserDetail customUserDetail,
        ReservationRequestDto reservationRequestDto, Long storeId) {

        User user = userRepository.findById(customUserDetail.getId())
            .orElseThrow(() -> new CustomRuntimeException(
                ExceptionCode.CANT_FIND_DATA));

        Store store = storeRepository.findById(storeId)
            .orElseThrow(() -> new CustomRuntimeException(ExceptionCode.CANT_FIND_DATA));

        Reservation reservation = new Reservation(
            reservationRequestDto.getPeopleNumber(),
            reservationRequestDto.getReservationTime(),
            store,
            user
        );

        reservationRepository.save(reservation);

        return ReservationResponseDto.from(reservation);
    }


}
