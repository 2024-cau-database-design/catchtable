package com.example.catchtable.domain;

import static com.example.catchtable.util.LocalDateTimeUtil.toLocalDateTimeOrNull;

import jakarta.validation.constraints.NotNull;
import java.sql.Timestamp;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RestaurantLocation {

  @NotNull
  private Integer restaurantId;

  @NotNull
  private BigDecimal latitude;

  @NotNull
  private BigDecimal longitude;

  @NotNull
  private LocalDateTime createdAt;

  @NotNull
  private LocalDateTime updatedAt;

  @NotNull
  private Boolean isDeleted;


  private LocalDateTime deletedAt;

  // 정적 팩토리 메소드
  public static RestaurantLocation fromEntity(
      final Integer restaurantId,
      final BigDecimal latitude, final BigDecimal longitude,
      final Timestamp createdAt, final Timestamp updatedAt,
      final Boolean isDeleted, final Timestamp deletedAt) {
    RestaurantLocation restaurantLocation = new RestaurantLocation();
    restaurantLocation.restaurantId = restaurantId;
    restaurantLocation.latitude = latitude;
    restaurantLocation.longitude = longitude;
    restaurantLocation.createdAt = createdAt.toLocalDateTime();
    restaurantLocation.updatedAt = updatedAt.toLocalDateTime();
    restaurantLocation.isDeleted = isDeleted;
    restaurantLocation.deletedAt = toLocalDateTimeOrNull(deletedAt);
    return restaurantLocation;
  }

  // ... (필요한 비즈니스 로직 추가)
}
