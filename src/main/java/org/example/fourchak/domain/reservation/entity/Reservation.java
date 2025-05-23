package org.example.fourchak.domain.reservation.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.fourchak.common.BaseEntity;
import org.example.fourchak.domain.coupon.entity.UserCoupon;
import org.example.fourchak.domain.store.entity.Store;
import org.example.fourchak.domain.user.entity.User;


@Getter
@Entity
@Table(name = "reservation")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int peopleNumber;

    @Column(nullable = false)
    private LocalDateTime reservationTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "user_coupon_id", nullable = true)
    private UserCoupon userCoupon;

    public Reservation(int peopleNumber, LocalDateTime reservationTime, Store store, User user) {
        this.peopleNumber = peopleNumber;
        this.reservationTime = reservationTime;
        this.store = store;
        this.user = user;
    }

    public Reservation(int peopleNumber, LocalDateTime reservationTime, Store store, User user,
        UserCoupon userCoupon) {
        this.peopleNumber = peopleNumber;
        this.reservationTime = reservationTime;
        this.store = store;
        this.user = user;
        this.userCoupon = userCoupon;
    }
}
