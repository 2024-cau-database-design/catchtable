package com.example.catchtable.domain;

import static com.example.catchtable.util.LocalDateTimeUtil.toLocalDateTimeOrNull;

import jakarta.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WaitingHistory {

  @NotNull
  private Integer id;

  @NotNull
  private Integer waitingId;

  @NotNull
  private LocalDateTime createdAt;

  @NotNull
  private Integer waitingStatusId;

  // 정적 팩토리 메소드
  public static WaitingHistory fromEntity(
      final Integer id, final Integer waitingId, final Timestamp createdAt, final Integer waitingStatusId) {
    WaitingHistory waitingHistory = new WaitingHistory();
    waitingHistory.id = id;
    waitingHistory.waitingId = waitingId;
    waitingHistory.createdAt = toLocalDateTimeOrNull(createdAt);
    waitingHistory.waitingStatusId = waitingStatusId;
    return waitingHistory;
  }
}