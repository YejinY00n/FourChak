package org.example.fourchak.domain.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.fourchak.domain.coupon.entity.Coupon;

@Getter
@AllArgsConstructor
public class CouponAdminResponseDto {

    private Long couponId;
    private int discount;
    private int count;

    public static CouponAdminResponseDto from(Coupon coupon) {
        return new CouponAdminResponseDto(
            coupon.getId(), coupon.getDiscount(), coupon.getCount());
    }
}
