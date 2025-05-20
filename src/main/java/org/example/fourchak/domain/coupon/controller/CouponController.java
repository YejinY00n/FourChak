package org.example.fourchak.domain.coupon.controller;

import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.example.fourchak.common.ResponseMessage;
import org.example.fourchak.config.security.CustomUserPrincipal;
import org.example.fourchak.domain.coupon.dto.request.CouponCreateRequestDto;
import org.example.fourchak.domain.coupon.dto.request.CouponUpdateRequestDto;
import org.example.fourchak.domain.coupon.dto.response.CouponResponseDto;
import org.example.fourchak.domain.coupon.service.CouponService;
import org.example.fourchak.domain.user.enums.UserRole;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PostMapping("/stores/{storeId}/coupons")
    ResponseEntity<ResponseMessage<CouponResponseDto>> createCoupon(
        @AuthenticationPrincipal CustomUserPrincipal userPrincipal, @PathVariable Long storeId,
        @RequestBody CouponCreateRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            ResponseMessage.success(
                couponService.createCoupon(userPrincipal.getId(), storeId, requestDto)));
    }

    @GetMapping("/stores/{storeId}/coupons")
    ResponseEntity<ResponseMessage<?>> findCoupon(
        @AuthenticationPrincipal CustomUserPrincipal userPrincipal, @PathVariable Long storeId) {
        Collection<? extends GrantedAuthority> authorities = userPrincipal.getAuthorities();
        if (authorities.stream().anyMatch(
            auth -> auth.getAuthority().equals("ROLE_" + UserRole.USER))) {
            return ResponseEntity.ok(
                ResponseMessage.success(couponService.findCoupon(storeId)));
        }
        if (authorities.stream().anyMatch(
            auth -> auth.getAuthority().equals("ROLE_" + UserRole.OWNER))) {
            return ResponseEntity.ok(
                ResponseMessage.success(couponService.findCouponWithAuthor(storeId)));
        }

        return ResponseEntity.badRequest().build();
    }

    @PatchMapping("/stores/{storeId}/coupons/{couponId}")
    ResponseEntity<ResponseMessage<Void>> updateCoupon(
        @PathVariable Long couponId, @RequestBody CouponUpdateRequestDto requestDto) {
        couponService.updateCoupon(couponId, requestDto);

        return ResponseEntity.ok(ResponseMessage.success());
    }

    @DeleteMapping("/stores/{storeId}/coupons/{couponId}")
    ResponseEntity<ResponseMessage<Void>> deleteCoupon(@PathVariable Long couponId) {
        couponService.deleteCoupon(couponId);
        return ResponseEntity.ok(ResponseMessage.success());
    }
}
