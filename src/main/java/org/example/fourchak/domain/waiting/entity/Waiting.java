package org.example.fourchak.domain.waiting.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.fourchak.common.BaseEntity;
import org.example.fourchak.domain.store.entity.Store;
import org.example.fourchak.domain.user.entity.User;
import org.example.fourchak.domain.waiting.enums.Status;

@Entity
@Getter
@NoArgsConstructor
public class Waiting extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int waitingNumber;

    private LocalDateTime reservationTime;

    private int peopleNum;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    public Waiting(int waitingNumber, LocalDateTime reservationTime, int peopleNum,
        Status status, User user, Store store) {
        this.waitingNumber = waitingNumber;
        this.reservationTime = reservationTime;
        this.peopleNum = peopleNum;
        this.status = status;
        this.user = user;
        this.store = store;
    }

    /*
     * 정적 팩토리 메서드
     */
    public static Waiting of(int waitingNumber, LocalDateTime reservationTime, int peopleNumber,
        Status status, User user, Store store) {
        return new Waiting(waitingNumber, reservationTime, peopleNumber, status, user, store);
    }

    @SuppressWarnings("lombok")
    public void setWaitingNumber(int waitingNumber) {
        this.waitingNumber = waitingNumber;
    }

    @SuppressWarnings("lombok")
    public void setStatus(Status status) {
        this.status = status;
    }
}
