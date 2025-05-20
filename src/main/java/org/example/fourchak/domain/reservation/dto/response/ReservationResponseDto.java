package org.example.fourchak.domain.reservation.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.fourchak.domain.coupon.entity.UserCoupon;
import org.example.fourchak.domain.reservation.entity.Reservation;


@Getter
@AllArgsConstructor
public class ReservationResponseDto {

    private Long reservationId;
    private Long userId;
    private Long storeId;
    private Long userCouponId;
    private Integer discount;
    private String userName;
    private String storeName;
    private int peopleNumber;
    private LocalDateTime reserveTime;
    private LocalDateTime createdAt;

    public static ReservationResponseDto from(Reservation reservation) {

        UserCoupon userCoupon = reservation.getUserCoupon();

        Long userCouponId = (userCoupon != null && userCoupon.getCoupon() != null)
            ? userCoupon.getCoupon().getId()
            : null;

        Integer discount = (userCoupon != null && userCoupon.getCoupon() != null)
            ? userCoupon.getCoupon().getDiscount()
            : null;

        return new ReservationResponseDto(
            reservation.getId(),
            reservation.getUser().getId(),
            reservation.getStore().getId(),
            userCouponId,
            discount,
            reservation.getUser().getUsername(),
            reservation.getStore().getStoreName(),
            reservation.getPeopleNumber(),
            reservation.getReservationTime(),
            reservation.getCreatedAt()
        );
    }

}
