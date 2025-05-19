package org.example.fourchak.domain.search.service;

import lombok.RequiredArgsConstructor;
import org.example.fourchak.domain.search.repository.SearchKeywordRepository;
import org.example.fourchak.domain.store.dto.response.StoreResponseDto;
import org.example.fourchak.domain.store.entity.Store;
import org.example.fourchak.domain.store.repository.StoreRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final SearchKeywordRepository searchKeywordRepository;
    private final StoreRepository storeRepository;

    // v1 검색 API - 캐시 사용하지 않음
    @Transactional
    public Page<StoreResponseDto> searchStore(String keyword, Pageable pageable) {
        // 검색어 저장 또는 카운트 증가
        saveSearchKeyword(keyword);

        Page<Store> storePage = storeRepository.findByStoreNameContaining(keyword, pageable);

        return storePage.map(StoreResponseDto::from);

    }

}
