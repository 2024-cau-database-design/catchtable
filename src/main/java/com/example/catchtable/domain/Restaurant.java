package com.example.catchtable.domain;

import static com.example.catchtable.util.LocalDateTimeUtil.toLocalDateTimeOrNull;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant {

  private Long id;  // int unsigned -> Long
  private String name;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Boolean isDeleted;
  private LocalDateTime deletedAt;
  private Long ownerId;  // int unsigned -> Long

  // 정적 팩토리 메소드
  public static Restaurant fromEntity(
      final Long id, final String name,  // int unsigned -> Long
      final Timestamp createdAt, final Timestamp updatedAt,
      final Boolean isDeleted, final Timestamp deletedAt,
      final Long ownerId) {  // int unsigned -> Long
    Restaurant restaurant = new Restaurant();
    restaurant.id = id;
    restaurant.name = name;
    restaurant.createdAt = createdAt.toLocalDateTime();
    restaurant.updatedAt = updatedAt.toLocalDateTime();
    restaurant.isDeleted = isDeleted;
    restaurant.deletedAt = toLocalDateTimeOrNull(deletedAt);
    restaurant.ownerId = ownerId;
    return restaurant;
  }
}