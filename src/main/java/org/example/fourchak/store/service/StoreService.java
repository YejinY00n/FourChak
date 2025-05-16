package org.example.fourchak.store.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.fourchak.store.dto.request.StoreRequestDto;
import org.example.fourchak.store.dto.response.StoreResponseDto;
import org.example.fourchak.store.entity.Store;
import org.example.fourchak.store.repository.StoreRepository;
import org.springframework.stereotype.Service;

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

        return new StoreResponseDto.from(savedstore);
    }

    // 가게 조회
    @Transactional
    public StoreResponseDto findStoreById(Long id) {
        Store store = storeRepository.findStoreByIdOrElseThrow(id);
        return new StoreResponseDto.from(store);
    }

    // 가게 정보수정
    @Transactional
    public StoreResponseDto updateStore(Long id, StoreRequestDto requestDto) {
        Store store = storeRepository.findStoreByIdOrElseThrow(id);
        User user = userRepository.findUserByOnwerIdOrElseThrow(userId);
        if (store.getUser() != user) {
            throw new IllegalArgumentException("해당 가게를 관리하는 사람이 아닙니다.");
        }
        store.updateStore(requestDto.getStoreName(), requestDto.getNumber(),
            requestDto.getSeatCount());
        return new StoreResponseDto.from(store);
    }

    // 가게 폐업
    @Transactional
    public void deleteStore(Long id) {
        Store store = storeRepository.findStoreByIdOrElseThrow(id);
        User user = userRepository.findUserByOnwerIdOrElseThrow(userId);

        if (store.getUser() != user) {
            throw new IllegalArgumentException("해당 가게를 관리하는 사람이 아닙니다.");
        }

        store.closeStore();
    }

}
