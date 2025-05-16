package org.example.fourchak.store.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.example.fourchak.common.BaseEntity;

@Entity
@Getter
@Table(name = "store")
public class Store extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String storeName;

    @Column(nullable = false)
    private String number;

    @Column(nullable = false)
    private int seatCount;

    @Column
    private LocalDateTime deletedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "store")
    private List<Coupon> coupons = new ArrayList<>();

    @OneToMany(mappedBy = "store")
    private List<Reservation> reservations = new ArrayList<>();

    public Store() {
    }

    public Store(String storeName, String number, int seatCount, User user) {
        this.storeName = storeName;
        this.number = number;
        this.seatCount = seatCount;
        this.user = user;
    }

    public void updateStore(String storeName, String number, int seatCount) {
        this.storeName = storeName;
        this.number = number;
        this.seatCount = seatCount;
    }

    public void closeStore() {
        this.deletedAt = LocalDateTime.now();
    }
}
