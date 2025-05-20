package org.example.fourchak.domain.waiting.service;


import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.fourchak.domain.reservation.dto.event.DeleteReservationEvent;
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

        Waiting waiting = Waiting.of(waitingNumber, reservationTime, peopleNum, user, store);

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
        List<Waiting> myWaits = waitingRepository.findByUserIdAndStatus(userId);

        //Entity -> DTO 변환
        return myWaits.stream()
            .map(
                wait -> new GetMyWaitingResponse(wait.getWaitingNumber(), wait.getReservationTime(),
                    wait.getPeopleNum(), wait.getStore().getId()))
            .collect(Collectors.toList());
    }

    /*
    Full Reservation 상태일 때 예약삭제 이벤트 발생시 자동 예약대기 삭제 및 예약
    AutomaticReservationFacade 에서 @EventListener 와 함께 사용됨
     DeleteReservationEvent 는 예약시간, storeId, userId, 예약 좌석 수
     */
    public void autoDeleteWaiting(DeleteReservationEvent event, int availableSeat) {
        // event 에서 storeId와 reservationTime 받아오기
        // waiting 둘로 찾아오기
        LocalDateTime reservationTime = event.getReservationTime();
        Long storeId = event.getStoreId();

        // 순위에 상관 없이 남은 자리가 예약자 수 보다 작거나 같은 사람부터 예약 등록
        List<Waiting> waitingList = waitingRepository.findByStoreIdAndReservationTime(
            storeId, reservationTime);

        int index = 0;
        for (Waiting wait : waitingList) {
            if (wait.getPeopleNum() <= availableSeat) {
                break;
            }
            index++;
        }
        Waiting nextTeam = waitingList.get(index);

        waitingRepository.delete(nextTeam);
    }

    @Transactional
    @Scheduled(cron = "0 0 * * * *")
    public void deleteExpired() {
        waitingRepository.deleteByReservationTimeBefore(LocalDateTime.now());
    }
}
