package org.example.fourchak.domain.search.controller;


import lombok.RequiredArgsConstructor;
import org.example.fourchak.common.ResponseMessage;
import org.example.fourchak.domain.search.service.SearchService;
import org.example.fourchak.domain.store.dto.response.StoreResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/api/v1/stores/search")
    public ResponseEntity<ResponseMessage<Page<StoreResponseDto>>> searchStoresV1
        (@RequestParam String keyword, @PageableDefault(size = 10) Pageable pageable) {

        Page<StoreResponseDto> responseDtos = searchService.searchStore(keyword, pageable);

        ResponseMessage<Page<StoreResponseDto>> responseMessage = ResponseMessage.<Page<StoreResponseDto>>builder()
            .statusCode(HttpStatus.OK.value())
            .message("가게 검색이 완료되었습니다.")
            .data(responseDtos)
            .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

}
