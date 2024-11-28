package com.example.catchtable.domain;

import static com.example.catchtable.util.LocalDateTimeUtil.toLocalDateTimeOrNull;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.sql.Timestamp;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RestaurantReview {

  private Integer id;
  private Integer restaurantId;
  private Integer customerId;
  private Integer reviewRating;
  private String reviewCaption;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Boolean isDeleted;
  private LocalDateTime deletedAt;

  // 정적 팩토리 메소드
  public static RestaurantReview fromEntity(Integer id, Integer restaurantId, Integer customerId,
      Integer reviewRating, String reviewCaption,
      Timestamp createdAt, Timestamp updatedAt, Boolean isDeleted, Timestamp deletedAt) {
    RestaurantReview restaurantReview = new RestaurantReview();
    restaurantReview.id = id;
    restaurantReview.restaurantId = restaurantId;
    restaurantReview.customerId = customerId;
    restaurantReview.reviewRating = reviewRating;
    restaurantReview.reviewCaption = reviewCaption;
    restaurantReview.createdAt = createdAt.toLocalDateTime();
    restaurantReview.updatedAt = updatedAt.toLocalDateTime();
    restaurantReview.isDeleted = isDeleted;
    restaurantReview.deletedAt = toLocalDateTimeOrNull(deletedAt);
    return restaurantReview;
  }

}