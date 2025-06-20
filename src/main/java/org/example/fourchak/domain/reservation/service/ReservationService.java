package org.example.fourchak.domain.reservation.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.fourchak.common.annotation.RedissonLock;
import org.example.fourchak.common.error.CustomRuntimeException;
import org.example.fourchak.common.error.ExceptionCode;
import org.example.fourchak.config.security.CustomUserPrincipal;
import org.example.fourchak.domain.coupon.entity.UserCoupon;
import org.example.fourchak.domain.coupon.repository.UserCouponRepository;
import org.example.fourchak.domain.reservation.dto.event.DeleteReservationEvent;
import org.example.fourchak.domain.reservation.dto.requset.ReservationRequestDto;
import org.example.fourchak.domain.reservation.dto.response.ReservationResponseDto;
import org.example.fourchak.domain.reservation.entity.Reservation;
import org.example.fourchak.domain.reservation.repository.ReservationRepository;
import org.example.fourchak.domain.store.entity.Store;
import org.example.fourchak.domain.store.repository.StoreRepository;
import org.example.fourchak.domain.user.entity.User;
import org.example.fourchak.domain.user.repository.UserRepository;
import org.example.fourchak.domain.waiting.repository.WaitingRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final UserCouponRepository userCouponRepository;
    private final WaitingRepository waitingRepository;
    private final ApplicationEventPublisher eventPublisher;

    @RedissonLock(key = "'lock:reservation:' + #storeId + ':' + #dto.reservationTime")
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public ReservationResponseDto saveReservation(CustomUserPrincipal customUserDetail,
        ReservationRequestDto dto, Long storeId, Long userCouponId) {

        Store store = storeRepository.findById(storeId)
            .orElseThrow(() -> new CustomRuntimeException(ExceptionCode.CANT_FIND_DATA));

        int reservationPeopleCount = countReservationPeopleAtTime(store.getId(),
            dto.getReservationTime());

        if (store.getSeatCount() - reservationPeopleCount < dto.getPeopleNumber()) {
            throw new CustomRuntimeException(ExceptionCode.NO_SEAT_AVAILABLE);
        }

        User user = userRepository.findById(customUserDetail.getId())
            .orElseThrow(() -> new CustomRuntimeException(
                ExceptionCode.CANT_FIND_DATA));

        Reservation reservation;

        if (userCouponId != null) {
            UserCoupon userCoupon = userCouponRepository.findById(userCouponId)
                .orElseThrow(() -> new CustomRuntimeException(ExceptionCode.CANT_FIND_DATA));
            validCoupon(customUserDetail.getId(), storeId, userCoupon);

            reservation = new Reservation(
                dto.getPeopleNumber(),
                dto.getReservationTime(),
                store,
                user,
                userCoupon
            );

            userCoupon.changeUsed();
        } else {
            reservation = new Reservation(
                dto.getPeopleNumber(),
                dto.getReservationTime(),
                store,
                user
            );
        }

        reservationRepository.save(reservation);

        return ReservationResponseDto.from(reservation);
    }


    public List<ReservationResponseDto> findByStoreId(Long id) {
        return reservationRepository.findByStoreId(id).stream().map(ReservationResponseDto::from)
            .toList();
    }

    public List<ReservationResponseDto> findByUserID(Long id,
        CustomUserPrincipal customUserDetail) {

        if (!id.equals(customUserDetail.getId())) {
            throw new CustomRuntimeException(ExceptionCode.ACCESS_DENIED_ERROR);
        }

        return reservationRepository.findByUserId(id).stream().map(ReservationResponseDto::from)
            .toList();
    }

    @Transactional
    public void deleteReserve(Long id) {
        Reservation reservation = reservationRepository.findByIdOrElseThrow(id);
        Optional.ofNullable(reservation.getUserCoupon())
            .ifPresent(UserCoupon::changeUsed);
        reservationRepository.delete(reservation);
        // 대기자가 있는지 확인하고 있으면 예약 삭제 이벤트 발송
        if (waitingRepository.existsByStoreIdAndReservationTime(reservation.getStore().getId(),
            reservation.getReservationTime())) {
            eventPublisher.publishEvent(DeleteReservationEvent.from(reservation));
        }
    }

    @Transactional
    @Scheduled(cron = "0 0 * * * *")
    public void deleteExpired() {
        reservationRepository.deleteByReservationTimeBefore(LocalDateTime.now());

    }

    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public int countReservationPeopleAtTime(Long storeId, LocalDateTime reservationTime) {
        return reservationRepository.countBystoreIdAndReservationTime(storeId, reservationTime);
    }

    /*
    예약 취소시 대기 걸어놓은 유저의 자동 예약만을 위한 예약 저장 메서드
     */
    public void justSave(int peopleNumber, LocalDateTime reservationTime, Long storeId,
        Long userId) {
        Reservation reservation = new Reservation(peopleNumber, reservationTime,
            storeRepository.getReferenceById(storeId), userRepository.getReferenceById(userId));
        reservationRepository.save(reservation);
    }

    public void validCoupon(Long userId, Long storeId, UserCoupon userCoupon) {

        if (!userCoupon.getUser().getId().equals(userId)) {
            throw new CustomRuntimeException(ExceptionCode.UNAUTHORIZED_USERCOUPON_ACCESS);
        }

        if (!userCoupon.getCoupon().getStore().getId().equals(storeId)) {
            throw new CustomRuntimeException(ExceptionCode.MISMATCH_COUPON_STORE);
        }

        if (userCoupon.isUsed()) {
            throw new CustomRuntimeException(ExceptionCode.ALREADY_USED_COUPON);
        }
    }
}
