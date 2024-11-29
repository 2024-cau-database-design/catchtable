package com.example.catchtable.domain;

import java.sql.Time;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationTime {

  private Long id; // int unsigned -> Long
  private Long restaurantId; // int unsigned -> Long
  private Time time;

  // 정적 팩토리 메소드
  public static ReservationTime fromEntity(Long id, Time time, Long restaurantId) {
    ReservationTime reservationTime = new ReservationTime();
    reservationTime.id = id;
    reservationTime.time = time;
    reservationTime.restaurantId = restaurantId;
    return reservationTime;
  }

  // ... (필요한 비즈니스 로직 추가)
}
