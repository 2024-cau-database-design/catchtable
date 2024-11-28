package com.example.catchtable.domain;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PickupStatus {

  private Integer id;
  private String type;

  // 정적 팩토리 메소드
  public static PickupStatus fromEntity(final Integer id, final String type) {
    PickupStatus pickupStatus = new PickupStatus();
    pickupStatus.id = id;
    pickupStatus.type = type;
    return pickupStatus;
  }
}