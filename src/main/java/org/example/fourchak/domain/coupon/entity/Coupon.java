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
import lombok.Setter;
import org.example.fourchak.domain.coupon.dto.CouponCreateRequestDto;
import org.example.fourchak.store.entity.Store;

@Entity
@Getter
@Table(name = "coupon")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Column(nullable = false)
    private int discount;

    @Column(nullable = false)
    private int count;

    public Coupon(CouponCreateRequestDto requestDto) {
        this.discount = requestDto.getDiscount();
        this.count = requestDto.getCount();
    }
}
