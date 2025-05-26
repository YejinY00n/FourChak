package org.example.fourchak.domain.coupon.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.fourchak.common.annotation.LockKey;
import org.example.fourchak.common.annotation.WithLettuceLock;
import org.example.fourchak.common.annotation.WithRedissonLock;
import org.example.fourchak.common.error.BaseException;
import org.example.fourchak.common.error.ExceptionCode;
import org.example.fourchak.domain.coupon.dto.response.UserCouponResponse;
import org.example.fourchak.domain.coupon.entity.Coupon;
import org.example.fourchak.domain.coupon.entity.UserCoupon;
import org.example.fourchak.domain.coupon.lock.mysql.NameLockWithDataSource;
import org.example.fourchak.domain.coupon.lock.mysql.NameLockWithJdbcTemplate;
import org.example.fourchak.domain.coupon.lock.redis.LockService;
import org.example.fourchak.domain.coupon.repository.CouponRepository;
import org.example.fourchak.domain.coupon.repository.UserCouponRepository;
import org.example.fourchak.domain.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserCouponService {

    private final UserCouponRepository userCouponRepository;
    private final CouponRepository couponRepository;
    private final LockService lockService;
    private final NameLockWithJdbcTemplate nameLockWithJdbcTemplate;
    private final NameLockWithDataSource nameLockWithDataSource;

    @Lazy
    @Autowired
    private UserCouponService self;

    @Transactional
    public void issueCoupon(User user, Long couponId) {
        log.info("ISSUE COUPON CALLED");
        // 존재하는 쿠폰인지 확인
        Coupon coupon = couponRepository.findById(couponId)
            .orElseThrow(() -> new BaseException(ExceptionCode.NOT_FOUND_COUPON));

        // 이미 발급받았는지 확인
        if (hasIssuedCoupon(user.getId(), couponId)) {
            throw new BaseException(ExceptionCode.HAS_ISSUED_COUPON);
        }

        UserCoupon userCoupon = UserCoupon.from(user, coupon);

        // 쿠폰 수량 차감 후 저장
        coupon.use();               // 수량 남아있는 지 확인 후 차감
        log.info("NOW COUPON COUNT: " + coupon.getCount());
        couponRepository.save(coupon);
        userCouponRepository.save(userCoupon);
    }

    public void issueCouponLettuceWithService(User user, Long couponId) {
        String key = "coupon:" + couponId;
        try {
            lockService.executeWithLock(key, () -> {
                self.issueCoupon(user, couponId);
            });
        } catch (InterruptedException e) {
            throw new BaseException(ExceptionCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    @WithLettuceLock
    public void issueCouponLettuceWithAOP(User user, @LockKey Long couponId) {
        // 존재하는 쿠폰인지 확인
        Coupon coupon = couponRepository.findById(couponId)
            .orElseThrow(() -> new BaseException(ExceptionCode.NOT_FOUND_COUPON));

        // 이미 발급받았는지 확인
        if (hasIssuedCoupon(user.getId(), couponId)) {
            throw new BaseException(ExceptionCode.HAS_ISSUED_COUPON);
        }

        UserCoupon userCoupon = UserCoupon.from(user, coupon);

        // 쿠폰 수량 차감 후 저장
        coupon.use();               // 수량 남아있는 지 확인 후 차감
        log.info("NOW COUPON COUNT: " + coupon.getCount());
        couponRepository.save(coupon);
        userCouponRepository.save(userCoupon);
    }

    @Transactional
    @WithRedissonLock
    public void issueCouponRedissonWithAOP(User user, @LockKey Long couponId) {
        // 존재하는 쿠폰인지 확인
        Coupon coupon = couponRepository.findById(couponId)
            .orElseThrow(() -> new BaseException(ExceptionCode.NOT_FOUND_COUPON));

        // 이미 발급받았는지 확인
        if (hasIssuedCoupon(user.getId(), couponId)) {
            throw new BaseException(ExceptionCode.HAS_ISSUED_COUPON);
        }

        UserCoupon userCoupon = UserCoupon.from(user, coupon);

        // 쿠폰 수량 차감 후 저장
        coupon.use();               // 수량 남아있는 지 확인 후 차감
        log.info("NOW COUPON COUNT: " + coupon.getCount());
        couponRepository.save(coupon);
        userCouponRepository.save(userCoupon);
    }


    public void issueCouponWithNamedLockAndJdbc(User user, Long couponId) {
        String key = "coupon:" + couponId;

        nameLockWithJdbcTemplate.executeWithLock(
            key, 5, () -> {
                issueCoupon(user, couponId);
                return null;
            }
        );
    }

    public void issueCouponWithNamedLockAndDS(User user, Long couponId) {
        String key = "coupon:" + couponId;

        nameLockWithDataSource.executeWithLock(
            key, 3, () -> {
                issueCoupon(user, couponId);
                return null;
            }
        );
    }


    public List<UserCouponResponse> findUserCoupons(Long userId) {
        return userCouponRepository.findAllByUserId(userId).stream()
            .map(UserCouponResponse::new)
            .toList();
    }

    public List<UserCouponResponse> findUserCouponsByStore(Long userId, Long storeId) {
        return userCouponRepository.findAllByUserIdAndStoreId(userId, storeId).stream()
            .map(UserCouponResponse::new)
            .toList();
    }

    @Transactional
    public void deleteCoupon(Long userId, Long userCouponId) {
        UserCoupon userCoupon = userCouponRepository.findById(userCouponId)
            .orElseThrow(() -> new BaseException(ExceptionCode.NOT_FOUND_USERCOUPON));
        // 존재하는 쿠폰인지 확인
        Coupon coupon = userCoupon.getCoupon();

        // 사용자 소유인지 확인
        if (!hasCoupon(userId, userCouponId)) {
            throw new BaseException(ExceptionCode.UNAUTHORIZED_USERCOUPON_ACCESS);
        }
        // 유저 쿠폰 삭제
        userCouponRepository.delete(userCoupon);

        // 쿠폰 수량 다시 증가
        coupon.cancelIssuedCoupon();
        couponRepository.save(coupon);
    }

    private boolean hasCoupon(Long userId, Long userCouponId) {
        return userCouponRepository.existsByUserIdAndId(userId, userCouponId);
    }

    private boolean hasIssuedCoupon(Long userId, Long couponId) {
        return userCouponRepository.existsByUserIdAndCouponId(userId, couponId);
    }
}
