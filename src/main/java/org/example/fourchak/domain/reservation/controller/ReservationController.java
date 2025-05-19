package org.example.fourchak.domain.reservation.controller;

import lombok.RequiredArgsConstructor;
import org.example.fourchak.common.ResponseMessage;
import org.example.fourchak.config.security.CustomUserPrincipal;
import org.example.fourchak.domain.reservation.dto.requset.ReservationRequestDto;
import org.example.fourchak.domain.reservation.service.ReservationService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/store/{storeId}/reservation")
    public ResponseMessage<?> saveReservation(
        @PathVariable Long storeId,
        @AuthenticationPrincipal CustomUserPrincipal customUserDetail,
        @RequestBody ReservationRequestDto reservationRequestDto) {

        return ResponseMessage.builder()
            .statusCode(201)
            .message("예약되었습니다.")
            .data(reservationService.saveReservation(customUserDetail, reservationRequestDto,
                storeId))
            .build();
    }

    @GetMapping("/stores/{storeId}/reservation")
    public ResponseMessage<?> findByStoreId(
        @PathVariable Long storeId
    ) {
        return ResponseMessage.builder()
            .statusCode(200)
            .message("조회 성공")
            .data(reservationService.findByStoreId(storeId))
            .build();
    }

    @GetMapping("/users/{userId}/reservation")
    public ResponseMessage<?> findByUserId(
        @PathVariable Long userId,
        @AuthenticationPrincipal CustomUserPrincipal customUserDetail
    ) {
        return ResponseMessage.builder()
            .statusCode(200)
            .message("조회 성공")
            .data(reservationService.findByUserID(userId, customUserDetail))
            .build();
    }

    @DeleteMapping("/reservation/{reservationId}")
    public ResponseMessage<?> deleteReservation(
        @PathVariable Long reservationId
    ) {
        reservationService.deleteReserve(reservationId);
        return ResponseMessage.builder()
            .statusCode(200)
            .message("예약이 취소되었습니다.")
            .build();
    }
}
