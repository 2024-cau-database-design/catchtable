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
  @Setter // 더 좋은 방법 없을까?
  @NotNull
  private Integer id;

  @NotNull
  private String name;

  @NotNull
  private LocalDateTime createdAt;

  @NotNull
  private LocalDateTime updatedAt;

  @NotNull
  private Boolean isDeleted;

  @Null
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
