package org.example.fourchak.domain.store.service;


import lombok.RequiredArgsConstructor;
import org.example.fourchak.domain.store.dto.request.StoreRequestDto;
import org.example.fourchak.domain.store.dto.response.StoreResponseDto;
import org.example.fourchak.domain.store.entity.Store;
import org.example.fourchak.domain.store.repository.StoreRepository;
import org.example.fourchak.domain.user.entity.User;
import org.example.fourchak.domain.user.repository.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    private final UserRepository userRepository;


    // 가게 등록
    @Transactional
    public StoreResponseDto saveStore(StoreRequestDto requestDto, Long userId) {

        User user = userRepository.findUserByOnwerIdOrElseThrow(userId);

        Store store = new Store(requestDto.getStoreName(), requestDto.getNumber(),
            requestDto.getSeatCount(), user);
        Store savedstore = storeRepository.save(store);

        return StoreResponseDto.from(savedstore);
    }

    // 가게 조회
    @Transactional
    public StoreResponseDto findStoreById(Long id) {
        Store store = storeRepository.findStoreByIdOrElseThrow(id);
        return StoreResponseDto.from(store);
    }

    // 가게 정보수정
    @Transactional
    @CacheEvict(value = "storeSearch", allEntries = true) // 가게 정보가 수정되면 모든 검색 캐시 무효화
    public StoreResponseDto updateStore(Long userId, Long storeId, StoreRequestDto requestDto) {
        Store store = storeRepository.findStoreByIdOrElseThrow(storeId);
        User user = userRepository.findUserByOnwerIdOrElseThrow(userId);
        if (store.getUser() != user) {
            throw new IllegalArgumentException("해당 가게를 관리하는 사람이 아닙니다.");
        }
        store.updateStore(requestDto.getStoreName(), requestDto.getNumber(),
            requestDto.getSeatCount());
        return StoreResponseDto.from(store);
    }

    // 가게 폐업
    @Transactional
    @CacheEvict(value = "storeSearch", allEntries = true) // 가게가 폐업하면 모든 검색 캐시 무효화
    public void deleteStore(Long id, Long userId) {
        Store store = storeRepository.findStoreByIdOrElseThrow(id);
        User user = userRepository.findUserByOnwerIdOrElseThrow(userId);

        if (store.getUser() != user) {
            throw new IllegalArgumentException("해당 가게를 관리하는 사람이 아닙니다.");
        }

        store.closeStore();
    }

}
