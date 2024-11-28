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

  @NotNull
  private Integer id;

  @NotNull
  private Integer restaurantId;

  @NotNull
  private Integer customerId;

  @NotNull
  private Integer reviewRating;


  private String reviewCaption;

  @NotNull
  private LocalDateTime createdAt;

  @NotNull
  private LocalDateTime updatedAt;

  @NotNull
  private Boolean isDeleted;


  private LocalDateTime deletedAt;

  // 정적 팩토리 메소드
  public static RestaurantReview create(Integer restaurantId, Integer customerId,
      Integer reviewRating, String reviewCaption,
      Timestamp createdAt, Timestamp updatedAt, Boolean isDeleted, Timestamp deletedAt) {
    RestaurantReview restaurantReview = new RestaurantReview();
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