package com.example.catchtable.domain;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationTime {

  private Long id;
  private Integer restaurantId;
  private Time time;

  // 정적 팩토리 메소드
  public static ReservationTime fromEntity(Long id, Time time, Integer restaurantId) {
    ReservationTime reservationTime = new ReservationTime();
    reservationTime.id = id;
    reservationTime.time = time;
    reservationTime.restaurantId = restaurantId;
    return reservationTime;
  }

  // ... (필요한 비즈니스 로직 추가)
}
