package org.example.fourchak.domain.coupon.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.fourchak.domain.coupon.entity.Coupon;

@Getter
@AllArgsConstructor
public class CouponResponseDto {

    private Long couponId;
    private int discount;

    public static CouponResponseDto from(Coupon coupon) {
        return new CouponResponseDto(coupon.getId(), coupon.getDiscount());
    }
}
