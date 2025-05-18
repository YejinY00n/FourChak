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

    private int peopleNumber;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    public Waiting(int waitingNumber, LocalDateTime reservationTime, int peopleNumber,
        Status status) {
        this.waitingNumber = waitingNumber;
        this.reservationTime = reservationTime;
        this.peopleNumber = peopleNumber;
        this.status = status;
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
