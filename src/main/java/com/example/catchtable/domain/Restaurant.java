package com.example.catchtable.domain;

import static com.example.catchtable.util.LocalDateTimeUtil.toLocalDateTimeOrNull;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import java.sql.Timestamp;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import lombok.Setter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant {

  private Integer id;
  private String name;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Boolean isDeleted;
  private LocalDateTime deletedAt;

  // 정적 팩토리 메소드
  public static Restaurant fromEntity(
      final Integer id, final String name,
      final Timestamp createdAt, final Timestamp updatedAt,
      final Boolean isDeleted, final Timestamp deletedAt) {
    Restaurant restaurant = new Restaurant();
    restaurant.id = id;
    restaurant.name = name;
    restaurant.createdAt = createdAt.toLocalDateTime();
    restaurant.updatedAt = updatedAt.toLocalDateTime();
    restaurant.isDeleted = isDeleted;
    restaurant.deletedAt = toLocalDateTimeOrNull(deletedAt);
    return restaurant;
  }

  // ... (필요한 비즈니스 로직 추가)
}
