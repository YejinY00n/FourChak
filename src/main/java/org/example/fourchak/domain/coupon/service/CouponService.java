package org.example.fourchak.domain.coupon.service;

import lombok.RequiredArgsConstructor;
import org.example.fourchak.domain.coupon.dto.CouponCreateRequestDto;
import org.example.fourchak.domain.coupon.dto.CouponResponseDto;
import org.example.fourchak.domain.coupon.entity.Coupon;
import org.example.fourchak.domain.coupon.repository.CouponRepository;
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
        coupon.getStore() = storeRepository.findById(storeId);

        // 쿠폰 레포지토리에 저장
        couponRepository.save(coupon);
        return new CouponResponseDto(coupon);
    }

    private boolean isOwnedStore(Long userId, Long targetStoreId) {
        return storeRepository.findById(targetStoreId).getUser().getId().equals(userId);
    }
}
