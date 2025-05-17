package org.example.fourchak.domain.coupon.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.fourchak.common.ResponseMessage;
import org.example.fourchak.domain.coupon.dto.CouponAdminResponseDto;
import org.example.fourchak.domain.coupon.dto.CouponCreateRequestDto;
import org.example.fourchak.domain.coupon.dto.CouponResponseDto;
import org.example.fourchak.domain.coupon.dto.CouponUpdateRequestDto;
import org.example.fourchak.domain.coupon.service.CouponService;
import org.example.fourchak.domain.user.enums.UserRole;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    // TODO: @AuthUser 추가
    private final CouponService couponService;

    @PostMapping("/stores/{storeId}/coupons")
    ResponseEntity<ResponseMessage<Void>> createCoupon(
        @PathVariable Long storeId, @RequestBody CouponCreateRequestDto requestDto) {
        Long userId = 1L;   // TODO: JWT 들어오면 변경

        ResponseMessage<Void> responseMessage = ResponseMessage.<Void>builder()
            .statusCode(HttpStatus.CREATED.value())
            .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);
    }

    @GetMapping("/stores/{storeId}/coupons")
    ResponseEntity<ResponseMessage<?>> findCoupon(@PathVariable Long storeId) {
        UserRole role = UserRole.USER;  // TODO: JWT 들어오면 변경
        if (role.equals(UserRole.USER)) {
            ResponseMessage<List<CouponResponseDto>> responseMessage = ResponseMessage.<List<CouponResponseDto>>builder()
                .statusCode(HttpStatus.CREATED.value())
                .data(couponService.findCoupon(storeId))
                .build();
            return ResponseEntity.ok(responseMessage);
        }
        if (role.equals(UserRole.ADMIN)) {
            ResponseMessage<List<CouponAdminResponseDto>> responseMessage =
                ResponseMessage.<List<CouponAdminResponseDto>>builder()
                    .statusCode(HttpStatus.CREATED.value())
                    .data(couponService.findCouponWithAuthor(storeId))
                    .build();
            return ResponseEntity.ok(responseMessage);
        }
        return ResponseEntity.badRequest().build();
    }

    @PatchMapping("/stores/{storeId}/coupons/{couponId}")
    ResponseEntity<ResponseMessage<Void>> updateCoupon(
        @PathVariable Long couponId, @RequestBody CouponUpdateRequestDto requestDto) {
        couponService.updateCoupon(couponId, requestDto);

        ResponseMessage<Void> responseMessage = ResponseMessage.<Void>builder()
            .statusCode(HttpStatus.CREATED.value())
            .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);
    }

    @DeleteMapping("/stores/{storeId}/coupons/{couponId}")
    ResponseEntity<ResponseMessage<Void>> deleteCoupon(@PathVariable Long couponId) {
        couponService.deleteCoupon(couponId);

        ResponseMessage<Void> responseMessage = ResponseMessage.<Void>builder()
            .statusCode(HttpStatus.CREATED.value())
            .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);
    }
}
