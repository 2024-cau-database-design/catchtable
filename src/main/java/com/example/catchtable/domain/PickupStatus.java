package com.example.catchtable.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PickupStatus {

  private Long id; // int unsigned -> Long
  private String type;

  // 정적 팩토리 메소드
  public static PickupStatus fromEntity(final Long id, final String type) {
    PickupStatus pickupStatus = new PickupStatus();
    pickupStatus.id = id;
    pickupStatus.type = type;
    return pickupStatus;
  }

  // ... (필요한 비즈니스 로직 추가)
}