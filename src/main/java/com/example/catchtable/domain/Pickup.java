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

  @NotNull
  private Integer id;

  @NotNull
  private Integer pickupTimeId;

  @NotNull
  private LocalDate pickupDate;

  // @NotNull
  private Integer pickedAt;

  @NotNull
  private LocalDateTime createdAt;

  @NotNull
  private LocalDateTime updatedAt;

  @NotNull
  private Boolean isDeleted;


  private LocalDateTime deletedAt;

  // 정적 팩토리 메소드
  public static Pickup create(Integer pickedAt, Integer pickupTimeId, LocalDate pickupDate,
      Timestamp createdAt, Boolean isDeleted, Timestamp deletedAt) {
    Pickup pickup = new Pickup();
    pickup.pickedAt = pickedAt;
    pickup.pickupTimeId = pickupTimeId;
    pickup.pickupDate = pickupDate;
    pickup.createdAt = createdAt.toLocalDateTime();
    pickup.isDeleted = isDeleted;
    pickup.deletedAt = toLocalDateTimeOrNull(deletedAt);
    return pickup;
  }

  // ... (필요한 비즈니스 로직 추가)
}
