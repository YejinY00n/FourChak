package org.example.fourchak.domain.search.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.fourchak.domain.search.dto.response.PopularKeywordResponseDto;
import org.example.fourchak.domain.search.entity.SearchKeyword;
import org.example.fourchak.domain.search.repository.SearchKeywordRepository;
import org.example.fourchak.domain.store.dto.response.StoreResponseDto;
import org.example.fourchak.domain.store.entity.Store;
import org.example.fourchak.domain.store.repository.StoreRepository;
import org.springframework.cache.annotation.Cacheable;
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

        // DB에서 직접 검색
        Page<Store> storePage = storeRepository.findByStoreNameContaining(keyword, pageable);

        return storePage.map(StoreResponseDto::from);
    }

    // v2 검색 API - 캐시 사용
    @Transactional(readOnly = true)
    @Cacheable(value = "storeSearch", key = "#keyword + '_' + #pageable.pageNumber + '_' + #pageable.pageSize")
    public Page<StoreResponseDto> searchStoreWithCache(String keyword, Pageable pageable) {
        // 캐시를 적용한 메서드에서는 조회만 하고 검색어 저장은 컨트롤러에서 별도로 호출
        Page<Store> storePage = storeRepository.findByStoreNameContaining(keyword, pageable);

        return storePage.map(StoreResponseDto::from);
    }

    // 인기 검색어 조회 - 캐시 사용
    @Transactional(readOnly = true)
    @Cacheable(value = "popularKeywords")
    public List<PopularKeywordResponseDto> getPopularKeywords() {
        List<SearchKeyword> keywords = searchKeywordRepository.findTop10ByOrderByCountDesc();

        return keywords.stream()
            .map(k -> new PopularKeywordResponseDto(k.getKeyword(), k.getCount()))
            .collect(Collectors.toList());
    }

    // 검색어 저장 (중복 검색어는 카운트 증가)
    @Transactional
    public void saveSearchKeyword(String keyword) {
        searchKeywordRepository.findByKeyword(keyword)
            .ifPresentOrElse(
                SearchKeyword::increaseCount,
                () -> searchKeywordRepository.save(new SearchKeyword(keyword))
            );
    }

}
