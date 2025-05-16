package org.example.fourchak.domain.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.fourchak.domain.coupon.entity.Coupon;

@Getter
@AllArgsConstructor
public class CouponResponseDto {

    private Long couponId;
    private int discount;

    public CouponResponseDto(Coupon coupon) {
        this.couponId = coupon.getId();
        this.discount = coupon.getDiscount();
    }
}
