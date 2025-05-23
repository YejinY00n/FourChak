package org.example.fourchak.domain.waiting.repository;

import io.lettuce.core.dynamic.annotation.Param;
import java.time.LocalDateTime;
import java.util.List;
import org.example.fourchak.domain.waiting.entity.Waiting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WaitingRepository extends JpaRepository<Waiting, Long> {

    List<Waiting> findByStoreIdAndReservationTime(Long storeId, LocalDateTime time);

    @Query("SELECT w FROM Waiting w JOIN FETCH w.store  WHERE w.user.id =: userId")
    List<Waiting> findByUserIdAndStatus(@Param("userId") Long userId);

    void deleteByReservationTimeBefore(LocalDateTime reservationTime);

    boolean existsByStoreIdAndReservationTime(Long storeId, LocalDateTime reservationTime);
}