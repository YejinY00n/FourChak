package org.example.fourchak.domain.reservation.service;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.fourchak.common.error.CustomRuntimeException;
import org.example.fourchak.common.error.ExceptionCode;
import org.example.fourchak.domain.reservation.dto.requset.ReservationRequestDto;
import org.example.fourchak.domain.reservation.dto.response.ReservationResponseDto;
import org.example.fourchak.domain.reservation.entity.Reservation;
import org.example.fourchak.domain.store.entity.Store;
import org.example.fourchak.domain.store.repository.StoreRepository;
import org.example.fourchak.domain.user.entity.User;
import org.example.fourchak.domain.user.repository.UserRepository;
import org.example.fourchak.reservation.repository.ReservationRepository;
import org.springframework.scheduling.annotation.Scheduled;
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

    public List<ReservationResponseDto> findByStoreId(Long id) {
        return reservationRepository.findByStoreId(id).stream().map(ReservationResponseDto::from)
            .toList();
    }

    public List<ReservationResponseDto> findByUserID(Long id, CustomUserDetail customUserDetail) {

        if (!id.equals(customUserDetail.getId())) {
            throw new CustomRuntimeException(ExceptionCode.ACCESS_DENIED_ERROR);
        }

        return reservationRepository.findByUserId(id).stream().map(ReservationResponseDto::from)
            .toList();
    }

    @Transactional
    public void deleteReserve(Long id) {
        reservationRepository.delete(reservationRepository.findByIdOrElseThrow(id));
    }

    @Transactional
    @Scheduled(cron = "0 0 * * * *")
    public void deleteExpired() {
        reservationRepository.deleteByReservationTimeBefore(LocalDateTime.now());
    }
}
