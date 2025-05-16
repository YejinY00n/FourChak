package org.example.fourchak.domain.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.fourchak.domain.coupon.entity.Coupon;

@Getter
@AllArgsConstructor
public class CouponOwnerResponseDto {

    private Long couponId;
    private int discount;
    private int count;

    public CouponOwnerResponseDto(Coupon coupon) {
        this.couponId = coupon.getId();
        this.discount = coupon.getDiscount();
        this.count = coupon.getCount();
    }
}
