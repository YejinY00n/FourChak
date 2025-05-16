package org.example.fourchak.domain.coupon.controller;

import lombok.RequiredArgsConstructor;
import org.example.fourchak.domain.coupon.dto.CouponCreateRequestDto;
import org.example.fourchak.domain.coupon.dto.CouponResponseDto;
import org.example.fourchak.domain.coupon.service.CouponService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
        @PathVariable Long storeId, @RequestBody CouponCreateRequestDto requestDto) {
        Long userId = 1L;   // TODO: JWT 들어오면 변경
        return ResponseEntity.ok(couponService.createCoupon(userId, storeId, requestDto));
    }

    @GetMapping("/stores/{storeId}/coupons")
    ResponseEntity<?> findCoupon(@PathVariable Long storeId) {
        UserRole role = USERROLE;   // TODO: JWT 들어오면 변경
        if (role.eqauls(UserRole.ROLE)) {
            return ResponseEntity.ok(couponService.findCoupon(storeId));
        }
        if (role.eqauls(UserRole.OWNER)) {
            return ResponseEntity.ok(couponService.findOwnerCoupon(storeId));
        }
    }
}
