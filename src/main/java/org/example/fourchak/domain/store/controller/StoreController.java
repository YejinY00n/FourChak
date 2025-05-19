package org.example.fourchak.domain.store.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.fourchak.common.ResponseMessage;
import org.example.fourchak.config.security.CustomUserPrincipal;
import org.example.fourchak.domain.store.dto.request.StoreRequestDto;
import org.example.fourchak.domain.store.dto.response.StoreResponseDto;
import org.example.fourchak.domain.store.service.StoreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    // 가게 등록
    @PostMapping
    public ResponseEntity<ResponseMessage<StoreResponseDto>> saveStore(
        @Valid @RequestBody StoreRequestDto requestDto,
        @AuthenticationPrincipal CustomUserPrincipal user) {
        StoreResponseDto responseDto = storeService.saveStore(requestDto, user.getId());

        ResponseMessage<StoreResponseDto> responseMessage = ResponseMessage.<StoreResponseDto>builder()
            .statusCode(HttpStatus.CREATED.value())
            .message("가게가 등록되었습니다.")
            .data(responseDto)
            .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);
    }

    // 가게 단건조회
    @GetMapping("/{storeId}")
    public ResponseEntity<ResponseMessage<StoreResponseDto>> findStoreById(
        @PathVariable Long storeId) {
        StoreResponseDto responseDto = storeService.findStoreById(storeId);

        ResponseMessage<StoreResponseDto> responseMessage = ResponseMessage.<StoreResponseDto>builder()
            .statusCode(HttpStatus.OK.value())
            .message("가게 조회가 완료되었습니다.")
            .data(responseDto)
            .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

    // 가게 정보수정
    @PatchMapping("/{storeId}")
    public ResponseEntity<ResponseMessage<StoreResponseDto>> updateStore(@PathVariable Long storeId,
        @RequestBody StoreRequestDto requestDto,
        @AuthenticationPrincipal CustomUserPrincipal user) {
        StoreResponseDto responseDto = storeService.updateStore(user.getId(), storeId, requestDto);

        ResponseMessage<StoreResponseDto> responseMessage = ResponseMessage.<StoreResponseDto>builder()
            .statusCode(HttpStatus.OK.value())
            .message("가게 정보가 수정되었습니다.")
            .data(responseDto)
            .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

    // 가게 폐업하기
    @DeleteMapping("/{storeId}")
    public ResponseEntity<ResponseMessage<Void>> deleteStore(@PathVariable Long storeId,
        @AuthenticationPrincipal CustomUserPrincipal user) {
        storeService.deleteStore(storeId, user.getId());

        ResponseMessage<Void> responseMessage = ResponseMessage.<Void>builder()
            .statusCode(HttpStatus.OK.value())
            .message("가게가 폐업처리 되었습니다.")
            .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

}
