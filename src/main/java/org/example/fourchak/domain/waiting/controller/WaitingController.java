package org.example.fourchak.domain.waiting.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.fourchak.common.ResponseMessage;
import org.example.fourchak.config.security.CustomUserPrincipal;
import org.example.fourchak.domain.waiting.dto.request.RegisterWaitingRequest;
import org.example.fourchak.domain.waiting.dto.response.GetMyWaitingResponse;
import org.example.fourchak.domain.waiting.dto.response.RegisterWaitingResponse;
import org.example.fourchak.domain.waiting.service.WaitingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WaitingController {

    private final WaitingService waitingService;

    /*
    대기 등록
     */
    @PostMapping("/stores/{storeId}/waiting")
    public ResponseEntity<ResponseMessage<RegisterWaitingResponse>> register(
        @PathVariable Long storeId,
        @RequestBody RegisterWaitingRequest dto) {
        RegisterWaitingResponse response = waitingService.register(storeId, dto);

        ResponseMessage<RegisterWaitingResponse> responseMessage = ResponseMessage.<RegisterWaitingResponse>builder()
            .statusCode(HttpStatus.CREATED.value())
            .message(String.format("예약 대기가 완료되었습니다. 대기 순번은 %d번 입니다.", response.getWaitingNum()))
            .data(response)
            .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);
    }

    /*
    사용자 예약 대기 목록
     */
    @GetMapping("/me/waiting")
    public ResponseEntity<ResponseMessage<List<GetMyWaitingResponse>>> getMyWaiting(
        @AuthenticationPrincipal CustomUserPrincipal user) {

        List<GetMyWaitingResponse> response = waitingService.getMyWaiting(user.getId());

        ResponseMessage<List<GetMyWaitingResponse>> responseMessage = ResponseMessage.<List<GetMyWaitingResponse>>builder()
            .statusCode(HttpStatus.CREATED.value())
            .message("현재 예약 대기 목록입니다.")
            .data(response)
            .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);
    }

    @DeleteMapping("/waiting/{waitingId}")
    public ResponseEntity<String> delete(Long waitingId) {
        waitingService.delete(waitingId);
        return ResponseEntity.ok("대기 취소가 완료되었습니다.");
    }
}
