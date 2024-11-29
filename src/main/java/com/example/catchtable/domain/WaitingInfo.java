package com.example.catchtable.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WaitingInfo {

  private Long waitingId; // int unsigned -> Long
  private Integer partySize;
  private Long restaurantId; // int unsigned -> Long
  private Long customerId; // int unsigned -> Long

  // 정적 팩토리 메소드
  public static WaitingInfo fromEntity(
      final Long waitingId, final Integer partySize, final Long restaurantId, final Long customerId) {
    WaitingInfo waitingInfo = new WaitingInfo();
    waitingInfo.waitingId = waitingId;
    waitingInfo.partySize = partySize;
    waitingInfo.restaurantId = restaurantId;
    waitingInfo.customerId = customerId;
    return waitingInfo;
  }

  // ... (필요한 비즈니스 로직 추가)
}