package org.example.fourchak.domain.coupon.repository;

import java.util.List;
import org.example.fourchak.domain.coupon.entity.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {

    @Query("SELECT uc FROM UserCoupon uc JOIN FETCH uc.user WHERE uc.user.id = :userId")
    List<UserCoupon> findAllByUserId(@Param("userId") Long userId);

    @Query("SELECT uc FROM UserCoupon uc JOIN FETCH uc.user "
        + "WHERE uc.user.id = :userId AND uc.coupon.store.id = :storeId")
    List<UserCoupon> findAllByUserIdAndStoreId(
        @Param("userId") Long userId, @Param("storeId") Long storeId);

    boolean existsByUserIdAndId(Long userId, Long userCouponId);

    boolean existsByUserIdAndCouponId(Long userId, Long couponId);
}
