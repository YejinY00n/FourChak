package org.example.fourchak.domain.coupon.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.fourchak.common.error.BaseException;
import org.example.fourchak.common.error.ExceptionCode;
import org.example.fourchak.domain.coupon.dto.response.UserCouponResponse;
import org.example.fourchak.domain.coupon.entity.Coupon;
import org.example.fourchak.domain.coupon.entity.UserCoupon;
import org.example.fourchak.domain.coupon.repository.CouponRepository;
import org.example.fourchak.domain.coupon.repository.UserCouponRepository;
import org.example.fourchak.domain.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserCouponService {

    private final UserCouponRepository userCouponRepository;
    private final CouponRepository couponRepository;

    @Transactional
    public void issueCoupon(User user, Long couponId) {
        // 존재하는 쿠폰인지 확인
        Coupon coupon = couponRepository.findById(couponId)
            .orElseThrow(() -> new BaseException(ExceptionCode.NOT_FOUND_COUPON));

        // 이미 발급받았는지 확인
        if (!hasIssuedCoupon(user.getId(), couponId)) {
            throw new BaseException(ExceptionCode.HAS_ISSUED_COUPON);
        }

        UserCoupon userCoupon = UserCoupon.from(user, coupon);

        // 쿠폰 수량 차감 후 저장
        coupon.use();               // 수량 남아있는 지 확인 후 차감
        userCouponRepository.save(userCoupon);
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

    public void deleteCoupon(Long userId, Long userCouponId) {
        UserCoupon userCoupon = userCouponRepository.findById(userCouponId)
            .orElseThrow(() -> new BaseException(ExceptionCode.NOT_FOUND_USERCOUPON));

        // 사용자 소유인지 확인
        if (!hasCoupon(userId, userCouponId)) {
            throw new BaseException(ExceptionCode.UNAUTHORIZED_USERCOUPON_ACCESS);
        }

        userCouponRepository.delete(userCoupon);
    }

    private boolean hasCoupon(Long userId, Long userCouponId) {
        return userCouponRepository.existsByUserIdAndId(userId, userCouponId);
    }

    private boolean hasIssuedCoupon(Long userId, Long couponId) {
        return userCouponRepository.existsByUserIdAndCouponId(userId, couponId);
    }
}
