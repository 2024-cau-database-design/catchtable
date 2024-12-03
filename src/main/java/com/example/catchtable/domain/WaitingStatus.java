package com.example.catchtable.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WaitingStatus {

  private Long id; // int unsigned -> Long
  private String type;

  // 정적 팩토리 메소드
  public static WaitingStatus fromEntity(final Long id, final String type) {
    WaitingStatus waitingStatus = new WaitingStatus();
    waitingStatus.id = id;
    waitingStatus.type = type;
    return waitingStatus;
  }

  // ... (필요한 비즈니스 로직 추가)
}