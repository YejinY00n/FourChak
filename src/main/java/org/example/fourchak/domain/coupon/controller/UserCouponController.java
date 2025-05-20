package org.example.fourchak.domain.coupon.controller;

import lombok.RequiredArgsConstructor;
import org.example.fourchak.common.ResponseMessage;
import org.example.fourchak.config.security.CustomUserPrincipal;
import org.example.fourchak.domain.coupon.dto.response.CouponResponseDto;
import org.example.fourchak.domain.coupon.service.UserCouponService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserCouponController {

    private final UserCouponService userCouponService;

    // TODO: 추후 PreAuthorized 추가
    @PostMapping("/coupons/{couponId}/issue")
    ResponseEntity<ResponseMessage<CouponResponseDto>> issueUserCoupon(
        @AuthenticationPrincipal CustomUserPrincipal userPrincipal, @PathVariable Long couponId) {
        userCouponService.issueCoupon(userPrincipal.getUser(), couponId);
        return ResponseEntity.ok(ResponseMessage.success());
    }

    // 사용자의 쿠폰 조회
    @GetMapping("/users/me/coupons")
    ResponseEntity<ResponseMessage<?>> findUserCoupons(
        @AuthenticationPrincipal CustomUserPrincipal userPrincipal) {
        return ResponseEntity.ok(
            ResponseMessage.success(userCouponService.findUserCoupons(userPrincipal.getId())));
    }

    // 사용자의 특정 가게에서 발급받은 쿠폰 조회
    @GetMapping("/users/me/coupons/{storeId}")
    ResponseEntity<ResponseMessage<?>> findUserCouponsByStore(
        @AuthenticationPrincipal CustomUserPrincipal userPrincipal, @PathVariable Long storeId) {
        return ResponseEntity.ok(
            ResponseMessage.success(
                userCouponService.findUserCouponsByStore(userPrincipal.getId(), storeId)));
    }


    @DeleteMapping("/users/me/coupons/{userCouponId}")
    ResponseEntity<ResponseMessage<Void>> deleteUserCoupon(
        @AuthenticationPrincipal CustomUserPrincipal userPrincipal,
        @PathVariable Long userCouponId) {
        userCouponService.deleteCoupon(userPrincipal.getId(), userCouponId);
        return ResponseEntity.ok(ResponseMessage.success());
    }
}
