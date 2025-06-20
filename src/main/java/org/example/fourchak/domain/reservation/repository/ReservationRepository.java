package org.example.fourchak.domain.reservation.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.example.fourchak.common.error.CustomRuntimeException;
import org.example.fourchak.common.error.ExceptionCode;
import org.example.fourchak.domain.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    default Reservation findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(
            () -> new CustomRuntimeException(ExceptionCode.CANT_FIND_DATA));
    }

    List<Reservation> findByStoreId(Long id);

    List<Reservation> findByReservationTimeAndStoreId(LocalDateTime reservationTime, Long storeId);

    List<Reservation> findByUserId(Long id);

    void deleteByReservationTimeBefore(LocalDateTime now);

    int countBystoreIdAndReservationTime(Long storeId, LocalDateTime reservationTime);
}
