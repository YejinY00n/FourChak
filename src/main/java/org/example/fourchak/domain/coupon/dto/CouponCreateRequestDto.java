package org.example.fourchak.domain.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CouponCreateRequestDto {
  private final int discount;
  private final int count;
}
