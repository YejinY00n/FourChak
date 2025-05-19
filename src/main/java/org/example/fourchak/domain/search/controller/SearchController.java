package org.example.fourchak.domain.search.controller;


import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.fourchak.common.ResponseMessage;
import org.example.fourchak.domain.search.dto.response.PopularKeywordResponseDto;
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

    // v1 검색 API - 캐시 없음
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

    // v2 검색 API - 캐시 사용
    @GetMapping("/api/v2/stores/search")
    public ResponseEntity<ResponseMessage<Page<StoreResponseDto>>> searchStoresV2
    (@RequestParam String keyword, @PageableDefault(size = 10) Pageable pageable) {

        // 검색어 저장/카운트 증가 (별도로 호출)
        searchService.saveSearchKeyword(keyword);

        // 캐시 된 검색 실행
        Page<StoreResponseDto> responseDtos = searchService.searchStoreWithCache(keyword, pageable);

        ResponseMessage<Page<StoreResponseDto>> responseMessage = ResponseMessage.<Page<StoreResponseDto>>builder()
            .statusCode(HttpStatus.OK.value())
            .message("가게 검색이 완료되었습니다.(캐시)")
            .data(responseDtos)
            .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

    // 인기 검색어 조회 API
    @GetMapping("/api/popular-keywords")
    public ResponseEntity<ResponseMessage<List<PopularKeywordResponseDto>>> getPopularKeywords() {

        List<PopularKeywordResponseDto> responseDtos = searchService.getPopularKeywords();

        ResponseMessage<List<PopularKeywordResponseDto>> responseMessage = ResponseMessage.<List<PopularKeywordResponseDto>>builder()
            .statusCode(HttpStatus.OK.value())
            .message("인기 검색어 조회가 완료되었습니다.")
            .data(responseDtos)
            .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

}
