package com.example.catchtable.domain;

import static com.example.catchtable.util.LocalDateTimeUtil.toLocalDateTimeOrNull;

import jakarta.validation.constraints.NotNull;
import java.sql.Timestamp;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RestaurantImage {

  @NotNull
  private Integer id;

  @NotNull
  private Integer restaurantId;

  @NotNull
  private String name;

  @NotNull
  private String url;


  private String description;

  @NotNull
  private LocalDateTime createdAt;

  @NotNull
  private Boolean isDeleted;


  private LocalDateTime deletedAt;

  // 정적 팩토리 메소드
  public static RestaurantImage fromEntity(
      final Integer id, final Integer restaurantId, final String name, final String url, final String description,
      final Timestamp createdAt, final Boolean isDeleted, final Timestamp deletedAt) {
    RestaurantImage restaurantImage = new RestaurantImage();
    restaurantImage.id = id;
    restaurantImage.restaurantId = restaurantId;
    restaurantImage.name = name;
    restaurantImage.url = url;
    restaurantImage.description = description;
    restaurantImage.createdAt = createdAt.toLocalDateTime();
    restaurantImage.isDeleted = isDeleted;
    restaurantImage.deletedAt = toLocalDateTimeOrNull(deletedAt);
    return restaurantImage;
  }
}