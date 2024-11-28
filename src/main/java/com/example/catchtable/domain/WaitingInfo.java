package com.example.catchtable.domain;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WaitingInfo {

  @NotNull
  private Integer waitingId;

  @NotNull
  private Integer partySize;

  @NotNull
  private Integer restaurantId;

  @NotNull
  private Integer customerId;

  // 정적 팩토리 메소드
  public static WaitingInfo fromEntity(
      final Integer waitingId, final Integer partySize, final Integer restaurantId, final Integer customerId) {
    WaitingInfo waitingInfo = new WaitingInfo();
    waitingInfo.waitingId = waitingId;
    waitingInfo.partySize = partySize;
    waitingInfo.restaurantId = restaurantId;
    waitingInfo.customerId = customerId;
    return waitingInfo;
  }
}