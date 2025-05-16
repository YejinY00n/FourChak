package org.example.fourchak.domain.reservation.controller;

import lombok.RequiredArgsConstructor;
import org.example.fourchak.common.ResponseMessage;
import org.example.fourchak.reservation.dto.requset.ReservationRequestDto;
import org.example.fourchak.reservation.dto.response.ReservationResponseDto;
import org.example.fourchak.reservation.service.ReservationService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/store/{storeId}/reservation")
    public ResponseMessage<ReservationResponseDto> saveReservation(
        @PathVariable Long storeId,
        @AuthenticationPrincipal CustomUserDetail customUserDetail,
        @RequestBody ReservationRequestDto reservationRequestDto) {

        return ResponseMessage.<ReservationResponseDto>builder()
            .statusCode(201)
            .message("예약되었습니다.")
            .data(reservationService.saveReservation(customUserDetail, reservationRequestDto,
                storeId))
            .build();
    }

}
