package org.example.fourchak.domain.coupon.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.fourchak.common.error.BaseException;
import org.example.fourchak.common.error.ExceptionCode;
import org.example.fourchak.domain.coupon.dto.request.CouponCreateRequestDto;
import org.example.fourchak.domain.store.entity.Store;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "coupon")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Setter
    @Column(nullable = false)
    private int discount;

    @Column(nullable = false)
    private int count;


    public Coupon(int discount, int count) {
        this.discount = discount;
        this.count = count;
    }

    public static Coupon from(CouponCreateRequestDto requestDto) {
        return new Coupon(requestDto.getDiscount(), requestDto.getCount());
    }

    public void use() {
        if (isExists()) {
            this.count -= 1;
        } else {
            throw new BaseException(ExceptionCode.SOLD_OUT_COUPON);
        }
    }

    public boolean isExists() {
        return this.count > 0;
    }

    public void cancelIssuedCoupon() {
        this.count += 1;
    }
}
