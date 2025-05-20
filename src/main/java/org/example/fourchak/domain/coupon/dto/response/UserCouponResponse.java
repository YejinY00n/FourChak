package org.example.fourchak.domain.coupon.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.fourchak.domain.coupon.entity.UserCoupon;

@Getter
@AllArgsConstructor
public class UserCouponResponse {

    private Long userId;

    private Long userCouponId;

    private Long couponId;

    private Long storeId;

    private boolean isUsed;

    public UserCouponResponse(UserCoupon userCoupon) {
        this.userId = userCoupon.getUser().getId();
        this.userCouponId = userCoupon.getId();
        this.couponId = userCoupon.getCoupon().getId();
        this.storeId = userCoupon.getCoupon().getStore().getId();
        this.isUsed = userCoupon.isUsed();
    }

    public static UserCouponResponse from(UserCoupon userCoupon) {
        return new UserCouponResponse(userCoupon);
    }
}
