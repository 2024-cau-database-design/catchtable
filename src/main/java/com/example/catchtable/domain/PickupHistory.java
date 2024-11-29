package com.example.catchtable.domain;

import static com.example.catchtable.util.LocalDateTimeUtil.toLocalDateTimeOrNull;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PickupHistory {

  private Long id; // int unsigned -> Long
  private Long statusId; // int unsigned -> Long
  private LocalDateTime pickedAt; // datetime -> LocalDateTime
  private Long pickupId; // int unsigned -> Long
  private LocalDate pickupDate;

  // 정적 팩토리 메소드
  public static PickupHistory fromEntity(
      final Long id, final Long statusId, final Timestamp pickedAt, final Long pickupId, final LocalDate pickupDate) {
    PickupHistory pickupHistory = new PickupHistory();
    pickupHistory.id = id;
    pickupHistory.statusId = statusId;
    pickupHistory.pickedAt = toLocalDateTimeOrNull(pickedAt);
    pickupHistory.pickupId = pickupId;
    pickupHistory.pickupDate = pickupDate;
    return pickupHistory;
  }

  // ... (필요한 비즈니스 로직 추가)
}