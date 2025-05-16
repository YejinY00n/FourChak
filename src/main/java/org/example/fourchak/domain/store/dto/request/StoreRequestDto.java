package org.example.fourchak.domain.store.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class StoreRequestDto {

    @NotBlank(message = "이름을 입력해주세요.")
    private final String storeName;

    @NotBlank(message = "전화번호를 입력해주세요.")
    private final String number;

    @NotBlank(message = "좌석 수를 입력해주세요.")
    private final int seatCount;

    public StoreRequestDto(String storeName, String number, int seatCount) {
        this.storeName = storeName;
        this.number = number;
        this.seatCount = seatCount;
    }
}
