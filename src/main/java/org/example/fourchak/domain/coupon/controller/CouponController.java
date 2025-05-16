package org.example.fourchak.domain.coupon.controller;

import lombok.RequiredArgsConstructor;
import org.example.fourchak.domain.coupon.dto.CouponResponseDto;
import org.example.fourchak.domain.coupon.service.CouponService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CouponController {
  // TODO: @AuthUser 추가
  private final CouponService couponService;

  @PostMapping("/stores/{storeId}/coupons")
  ResponseEntity<CouponResponseDto> createCoupon(
      @PathVariable Long storeId, @RequestBody CreateCouponRequestDto requestDto) {
    return couponService.createCoupon(userId, storeId, requestDto);
  }
}
