package com.example.catchtable.domain;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PickupTime {

  @NotNull
  private Integer id;

  @NotNull
  private Integer restaurantId;

  @NotNull
  private Time time;

  // 정적 팩토리 메소드
  public static PickupTime create(Time time, Integer restaurantId) {
    PickupTime pickupTime = new PickupTime();
    pickupTime.time = time;
    pickupTime.restaurantId = restaurantId;
    return pickupTime;
  }

  // ... (필요한 비즈니스 로직 추가)
}
