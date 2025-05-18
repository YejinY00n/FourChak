package org.example.fourchak.domain.store.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.fourchak.domain.store.dto.request.StoreRequestDto;
import org.example.fourchak.domain.store.dto.response.StoreResponseDto;
import org.example.fourchak.domain.store.service.StoreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
    public ResponseEntity<StoreResponseDto> saveStore(
        @Valid @RequestBody StoreRequestDto requestDto) {
        StoreResponseDto responseDto = storeService.saveStore(requestDto, userId);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    // 가게 단건조회
    @PostMapping("/{storeId}")
    public ResponseEntity<StoreResponseDto> findStoreById(@PathVariable Long storeId) {
        StoreResponseDto responseDto = storeService.findStoreById(storeId);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 가게 정보수정
    @PatchMapping("/{storeId}")
    public ResponseEntity<StoreResponseDto> updateStore(@PathVariable Long storeId,
        @RequestBody StoreRequestDto requestDto) {
        StoreResponseDto responseDto = storeService.updateStore(storeId, requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 가게 폐업하기
    @DeleteMapping("/{storeId}")
    public ResponseEntity<Void> deleteStore(@PathVariable Long storeId) {
        storeService.deleteStore(storeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
