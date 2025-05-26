package org.example.fourchak.domain.coupon.repository;

import java.util.List;
import org.example.fourchak.domain.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

    @Query("SELECT c FROM Coupon c JOIN FETCH c.store WHERE c.store.id = :storeId")
    List<Coupon> findAllByStoreId(@Param("storeId") Long storeId);
}
