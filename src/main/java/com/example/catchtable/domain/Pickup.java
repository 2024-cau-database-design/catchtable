package com.example.catchtable.domain;

import static com.example.catchtable.util.LocalDateTimeUtil.toLocalDateTimeOrNull;

import jakarta.validation.constraints.NotNull;
import java.sql.Timestamp;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pickup {

  private Long id; // int unsigned -> Long
  private LocalDateTime pickedAt; // datetime -> LocalDateTime
  private Long pickupTimeId; // int unsigned -> Long
  private LocalDate pickupDate;
  private Long restaurantId; // int unsigned -> Long
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Boolean isDeleted;
  private LocalDateTime deletedAt;

  // 정적 팩토리 메소드
  public static Pickup fromEntity(
      final Long id, final Timestamp pickedAt, final Long pickupTimeId, final LocalDate pickupDate, // 타입 변경
      final Long restaurantId, // 필드 추가
      final Timestamp createdAt, final Timestamp updatedAt, final Boolean isDeleted, final Timestamp deletedAt) {
    Pickup pickup = new Pickup();
    pickup.id = id;
    pickup.pickedAt = toLocalDateTimeOrNull(pickedAt);
    pickup.pickupTimeId = pickupTimeId;
    pickup.pickupDate = pickupDate;
    pickup.restaurantId = restaurantId;
    pickup.createdAt = createdAt.toLocalDateTime();
    pickup.updatedAt = updatedAt.toLocalDateTime();
    pickup.isDeleted = isDeleted;
    pickup.deletedAt = toLocalDateTimeOrNull(deletedAt);
    return pickup;
  }

  // ... (필요한 비즈니스 로직 추가)
}
