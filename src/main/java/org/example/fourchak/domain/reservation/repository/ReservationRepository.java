package org.example.fourchak.reservation.repository;

import java.util.List;
import org.example.fourchak.common.error.CustomRuntimeException;
import org.example.fourchak.common.error.ExceptionCode;
import org.example.fourchak.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    default Reservation findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(
            () -> new CustomRuntimeException(ExceptionCode.CANT_FIND_DATA));
    }

    List<Reservation> findByStoreId(Long id);

    List<Reservation> findByUserId(Long id);


}
