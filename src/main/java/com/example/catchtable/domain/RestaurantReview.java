package com.example.catchtable.domain;

import static com.example.catchtable.util.LocalDateTimeUtil.toLocalDateTimeOrNull;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RestaurantReview {

  private Long id; // int unsigned -> Long
  private Long restaurantId; // int unsigned -> Long
  private Long customerId; // int unsigned -> Long
  private BigDecimal rating; // decimal(3,1) -> BigDecimal
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Boolean isDeleted;
  private LocalDateTime deletedAt;
  private String text;

  // 정적 팩토리 메소드
  public static RestaurantReview fromEntity(
      final Long id, final Long restaurantId, final Long customerId, final BigDecimal rating,
      final Timestamp createdAt, final Timestamp updatedAt, final Boolean isDeleted,
      final Timestamp deletedAt, final String text) {
    RestaurantReview restaurantReview = new RestaurantReview();
    restaurantReview.id = id;
    restaurantReview.restaurantId = restaurantId;
    restaurantReview.customerId = customerId;
    restaurantReview.rating = rating;
    restaurantReview.createdAt = createdAt.toLocalDateTime();
    restaurantReview.updatedAt = updatedAt.toLocalDateTime();
    restaurantReview.isDeleted = isDeleted;
    restaurantReview.deletedAt = toLocalDateTimeOrNull(deletedAt);
    restaurantReview.text = text;
    return restaurantReview;
  }

  // ... (필요한 비즈니스 로직 추가)
}