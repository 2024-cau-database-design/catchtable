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

  private Integer id;
  private Integer pickupTimeId;
  private LocalDate pickupDate;
  private Integer pickedAt;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Boolean isDeleted;
  private LocalDateTime deletedAt;

  // 정적 팩토리 메소드
  public static Pickup fromEntity(Integer id, Integer pickedAt, Integer pickupTimeId, LocalDate pickupDate,
      Timestamp createdAt, Timestamp updatedAt, Boolean isDeleted, Timestamp deletedAt) {
    Pickup pickup = new Pickup();
    pickup.id = id;
    pickup.pickedAt = pickedAt;
    pickup.pickupTimeId = pickupTimeId;
    pickup.pickupDate = pickupDate;
    pickup.createdAt = createdAt.toLocalDateTime();
    pickup.updatedAt = updatedAt.toLocalDateTime();
    pickup.isDeleted = isDeleted;
    pickup.deletedAt = toLocalDateTimeOrNull(deletedAt);
    return pickup;
  }

  // ... (필요한 비즈니스 로직 추가)
}
