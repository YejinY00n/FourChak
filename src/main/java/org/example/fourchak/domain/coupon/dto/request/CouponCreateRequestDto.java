package org.example.fourchak.domain.coupon.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Range;

@Getter
@AllArgsConstructor
public class CouponCreateRequestDto {

    @NotNull
    @Range(min = 0, max = 100, message = "할인율은 0~100% 사이입니다.")
    private final Integer discount;

    @NotNull
    @Max(1000000)
    @PositiveOrZero(message = "쿠폰 개수는 음수일 수 없습니다.")
    private final Integer count;
}
