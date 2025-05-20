package org.example.fourchak.domain.coupon.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Range;

@Getter
@AllArgsConstructor
public class CouponUpdateRequestDto {

    @NotNull
    @Range(min = 0, max = 100, message = "할인율은 0~100% 사이입니다.")
    private int discount;
}
