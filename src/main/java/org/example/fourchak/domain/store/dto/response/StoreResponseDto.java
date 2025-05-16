package org.example.fourchak.domain.store.dto.response;

import java.time.LocalDateTime;
import lombok.Getter;
import org.example.fourchak.domain.store.entity.Store;

@Getter
public class StoreResponseDto {

    private final Long id;
    private final String storeName;
    private final String number;
    private final int seatCount;
    private final int couponCount;
    private final LocalDateTime createdAt;


    public StoreResponseDto(Long id, String storeName, String number, int seatCount,
        int couponCount, LocalDateTime createdAt) {
        this.id = id;
        this.storeName = storeName;
        this.number = number;
        this.seatCount = seatCount;
        this.couponCount = couponCount;
        this.createdAt = createdAt;
    }

    // entity -> dto 변환하는 정적 팩토리 메서드
    public static StoreResponseDto from(Store store) {

        int totalCouponCount = store.getCoupons().stream()
            .mapToInt(coupon -> coupon.getCount()).sum();

        return new StoreResponseDto(store.getId(), store.getStoreName(), store.getNumber(),
            Integer.parseInt(store.getSeatCount()), totalCouponCount, store.getCreatedAt());
    }
}
