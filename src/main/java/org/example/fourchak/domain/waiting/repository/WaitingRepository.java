package org.example.fourchak.domain.waiting.repository;

import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.example.fourchak.domain.waiting.entity.Waiting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface WaitingRepository extends JpaRepository<Waiting, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT w FROM Waiting w WHERE w.store.id =:storeId AND w.reservationTime=: reservationTime")
    List<Waiting> findWithLockByStoreIdAndReservationTime(@Param("storeId") Long storeId,
        @Param("reservationTime") LocalDateTime time);

    @Query("SELECT w FROM Waiting w JOIN FETCH w.store  WHERE w.user.id =: userId")
    List<Waiting> findByUserIdAndStatus(@Param("userId") Long userId);

    void deleteByReservationTimeBefore(LocalDateTime reservationTime);

    @Query("SELECT w FROM Waiting w JOIN Reservation r ON w.user.id")
    Optional<Waiting> findByReservationId(@Param("reservationId") Long reservationId);

    boolean existsByStoreIdAndReservationTime(Long storeId, LocalDateTime reservationTime);

    int countByStoreIdAndReservationTime(Long storeId, LocalDateTime reservationTime);
}