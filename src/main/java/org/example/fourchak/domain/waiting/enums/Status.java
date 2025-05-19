package org.example.fourchak.domain.waiting.enums;

import lombok.Getter;

@Getter
public enum Status {
  CANCELLED("취소됨"),
  RESERVED("예약 완료"),
  ACTIVE("예약 대기중");

  private final String label;

  Status(String label) {
    this.label = label;
  }
}
