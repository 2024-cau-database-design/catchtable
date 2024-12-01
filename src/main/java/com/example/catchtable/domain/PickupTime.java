package com.example.catchtable.domain;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PickupTime {

  private Long id;
  private Integer restaurantId;
  private Time time;

  // 정적 팩토리 메소드
  public static PickupTime fromEntity(Long id, Time time, Integer restaurantId) {
    PickupTime pickupTime = new PickupTime();
    pickupTime.id = id;
    pickupTime.time = time;
    pickupTime.restaurantId = restaurantId;
    return pickupTime;
  }

  // ... (필요한 비즈니스 로직 추가)
}
