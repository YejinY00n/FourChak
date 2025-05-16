package org.example.fourchak.domain.coupon.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.fourchak.domain.coupon.dto.CouponCreateRequestDto;
import org.example.fourchak.domain.coupon.dto.CouponOwnerResponseDto;
import org.example.fourchak.domain.coupon.dto.CouponResponseDto;
import org.example.fourchak.domain.coupon.entity.Coupon;
import org.example.fourchak.domain.coupon.repository.CouponRepository;
import org.example.fourchak.store.entity.Store;
import org.example.fourchak.store.repository.StoreRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final StoreRepository storeRepository;

    public CouponResponseDto createCoupon(Long userId, Long storeId,
        CouponCreateRequestDto requestDto) {
        // 사용자의 소유 가게인지 확인
        if (!isOwnedStore(userId, storeId)) {
            // TODO: 전역 예외 처리기로 변경
            throw new IllegalArgumentException("본인 소유의 가게가 아닙니다.");
        }

        Coupon coupon = new Coupon(requestDto);
        Store store = storeRepository.findById(storeId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 가게입니다."));
        coupon.setStore(store);

        // 쿠폰 레포지토리에 저장
        couponRepository.save(coupon);
        return new CouponResponseDto(coupon);
    }

    // 일반 사용자 조회
    public List<CouponResponseDto> findCoupon(Long storeId) {
        Store store = storeRepository.findById(storeId).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 가게입니다."));
        List<Coupon> couponList = couponRepository.findAllByStore(store);
        return couponList.stream()
            .map(CouponResponseDto::new).toList();
    }

    // 가게 사장 조회
    public List<CouponOwnerResponseDto> findOwnerCoupon(Long storeId) {
        Store store = storeRepository.findById(storeId).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 가게입니다."));
        List<Coupon> couponList = couponRepository.findAllByStore(store);
        return couponList.stream()
            .map(CouponOwnerResponseDto::new).toList();
    }

    private boolean isOwnedStore(Long userId, Long targetStoreId) {
        return storeRepository.findById(targetStoreId).getUser().getId().equals(userId);
    }
}
