package org.example.fourchak.domain.waiting.dto.request;

import com.esotericsoftware.kryo.serializers.FieldSerializer.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterWaitingRequest {

    @NotNull
    private LocalDateTime reservationTime;
    @NotNull
    private int peopleNum;
    @NotNull
    private Long userId;

}
