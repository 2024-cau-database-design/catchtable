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
public class Waiting { // Waiting 오타 수정

  @NotNull
  private Integer id;

  @NotNull
  private LocalDateTime createdAt;

  @NotNull
  private Integer customerId;

  @NotNull
  private Integer guestCount;

  @NotNull
  private Integer restaurantId;

  // 정적 팩토리 메소드
  public static Waiting fromEntity(
      final Integer id, final Timestamp createdAt, final Integer customerId, final Integer guestCount, final Integer restaurantId) {
    Waiting waiting = new Waiting();
    waiting.id = id;
    waiting.createdAt = toLocalDateTimeOrNull(createdAt);
    waiting.customerId = customerId;
    waiting.guestCount = guestCount;
    waiting.restaurantId = restaurantId;
    return waiting;
  }
}