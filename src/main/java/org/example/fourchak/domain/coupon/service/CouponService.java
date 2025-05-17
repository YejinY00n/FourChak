package org.example.fourchak.domain.coupon.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.fourchak.domain.coupon.dto.CouponAdminResponseDto;
import org.example.fourchak.domain.coupon.dto.CouponCreateRequestDto;
import org.example.fourchak.domain.coupon.dto.CouponResponseDto;
import org.example.fourchak.domain.coupon.dto.CouponUpdateRequestDto;
import org.example.fourchak.domain.coupon.entity.Coupon;
import org.example.fourchak.domain.coupon.repository.CouponRepository;
import org.example.fourchak.domain.store.entity.Store;
import org.example.fourchak.domain.store.repository.StoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        Coupon coupon = Coupon.from(requestDto);
        Store store = storeRepository.findById(storeId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 가게입니다."));
        coupon.setStore(store);

        // 쿠폰 레포지토리에 저장
        couponRepository.save(coupon);
        return CouponResponseDto.from(coupon);
    }

    // 일반 사용자 조회
    public List<CouponResponseDto> findCoupon(Long storeId) {
        Store store = storeRepository.findById(storeId).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 가게입니다."));
        List<Coupon> couponList = couponRepository.findAllByStore(store);
        return couponList.stream()
            .map(CouponResponseDto::from).toList();
    }

    // 가게 사장 조회
    public List<CouponAdminResponseDto> findCouponWithAuthor(Long storeId) {
        Store store = storeRepository.findById(storeId).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 가게입니다."));
        List<Coupon> couponList = couponRepository.findAllByStore(store);
        return couponList.stream()
            .map(CouponAdminResponseDto::from).toList();
    }

    // 쿠폰 업데이트
    @Transactional
    public void updateCoupon(Long couponId, CouponUpdateRequestDto requestDto) {
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 쿠폰입니다."));
        coupon.setDiscount(requestDto.getDiscount());
    }

    // 쿠폰 삭제
    @Transactional
    public void deleteCoupon(Long couponId) {
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 쿠폰입니다."));
        couponRepository.delete(coupon);
    }

    private boolean isOwnedStore(Long userId, Long targetStoreId) {
        return storeRepository.findById(targetStoreId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 가게 입니다."))
            .getUser().getId().equals(userId);
    }
}
