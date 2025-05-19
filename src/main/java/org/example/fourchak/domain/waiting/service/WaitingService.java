package org.example.fourchak.domain.waiting.service;

import static org.example.fourchak.domain.waiting.enums.Status.ACTIVE;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.fourchak.domain.store.entity.Store;
import org.example.fourchak.domain.store.repository.StoreRepository;
import org.example.fourchak.domain.user.entity.User;
import org.example.fourchak.domain.user.repository.UserRepository;
import org.example.fourchak.domain.waiting.dto.request.RegisterWaitingRequest;
import org.example.fourchak.domain.waiting.dto.response.GetMyWaitingResponse;
import org.example.fourchak.domain.waiting.dto.response.RegisterWaitingResponse;
import org.example.fourchak.domain.waiting.entity.Waiting;
import org.example.fourchak.domain.waiting.repository.WaitingRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WaitingService {

    private final WaitingRepository waitingRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    public RegisterWaitingResponse register(Long storeId, RegisterWaitingRequest dto) {
        LocalDateTime reservationTime = dto.getReservationTime();
        int peopleNum = dto.getPeopleNum();
        Long userId = dto.getUserId();

        // 대기순위 결정
        List<Waiting> waitingList = waitingRepository.findByStoreIdAndReservationTime(storeId,
            reservationTime);
        int waitingNumber = waitingList.size() + 1;

        User user = userRepository.getReferenceById(userId);
        Store store = storeRepository.getReferenceById(storeId);

        Waiting waiting = Waiting.of(waitingNumber, reservationTime, peopleNum,
            ACTIVE, user, store);

        Waiting waitingEntity = waitingRepository.save(waiting);

        return RegisterWaitingResponse.builder()
            .waitingId(waitingEntity.getId())
            .waitingNum(waitingNumber)
            .reservationTime(reservationTime)
            .peopleNum(peopleNum)
            .userId(userId)
            .storeId(storeId)
            .build();
    }

    //TODO: 전역 생기면 예외 수정
    @Transactional
    public void delete(Long waitingId) {
        Waiting wait = waitingRepository.findById(waitingId)
            .orElseThrow(() -> new RuntimeException("예약이 없습니다."));
        waitingRepository.delete(wait);
    }

    /*
    현재 유저의 활성화 되어있는 예약 대기 목록 조회
     */
    public List<GetMyWaitingResponse> getMyWaiting(Long userId) {
        List<Waiting> myWaits = waitingRepository.findByUserIdAndStatus(userId, ACTIVE);

        //Entity -> DTO 변환
        return myWaits.stream()
            .map(
                wait -> new GetMyWaitingResponse(wait.getWaitingNumber(), wait.getReservationTime(),
                    wait.getPeopleNum(), wait.getStore().getId()))
            .collect(Collectors.toList());
    }

    @Transactional
    @Scheduled(cron = "0 0 * * * *")
    public void deleteExpired() {
        waitingRepository.deleteByReservationTimeBefore(LocalDateTime.now());
    }
}
