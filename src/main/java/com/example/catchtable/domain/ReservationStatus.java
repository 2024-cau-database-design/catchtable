package com.example.catchtable.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationStatus {

  private Long id; // int unsigned -> Long
  private String type;

  // 정적 팩토리 메소드
  public static ReservationStatus fromEntity(final Long id, final String type) {
    ReservationStatus reservationStatus = new ReservationStatus();
    reservationStatus.id = id;
    reservationStatus.type = type;
    return reservationStatus;
  }

  // ... (필요한 비즈니스 로직 추가)
}