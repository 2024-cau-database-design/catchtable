package com.example.catchtable.domain;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WaitingHistory {

  private Long id; // int unsigned -> Long
  private Long waitingId; // int unsigned -> Long
  private LocalDateTime createdAt; // int -> Long
  private Long waitingStatusId; // int unsigned -> Long

  // 정적 팩토리 메소드
  public static WaitingHistory fromEntity(
      final Long id, final Long waitingId, final Timestamp createdAt, final Long waitingStatusId) {
    WaitingHistory waitingHistory = new WaitingHistory();
    waitingHistory.id = id;
    waitingHistory.waitingId = waitingId;
    waitingHistory.createdAt = createdAt.toLocalDateTime();
    waitingHistory.waitingStatusId = waitingStatusId;
    return waitingHistory;
  }

  // ... (필요한 비즈니스 로직 추가)
}