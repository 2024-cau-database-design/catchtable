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
public class RestaurantInfo {

  @NotNull
  private Integer restaurantId;

  @NotBlank
  private String phoneNumber;

  @NotBlank
  private String fullAddress;


  private String websiteUrl;


  private String description;

  @NotNull
  private LocalDateTime createdAt;

  @NotNull
  private LocalDateTime updatedAt;

  @NotNull
  private Boolean isDeleted;


  private LocalDateTime deletedAt;

  // 정적 팩토리 메소드
  public static RestaurantInfo fromEntity(
      final Integer restaurantId, final String phoneNumber,
      final String fullAddress, final String websiteUrl,
      final String description, final Timestamp createdAt,
      final Timestamp updatedAt, final Boolean isDeleted, final Timestamp deletedAt) {
    RestaurantInfo restaurantInfo = new RestaurantInfo();
    restaurantInfo.restaurantId = restaurantId;
    restaurantInfo.phoneNumber = phoneNumber;
    restaurantInfo.fullAddress = fullAddress;
    restaurantInfo.websiteUrl = websiteUrl;
    restaurantInfo.description = description;
    restaurantInfo.createdAt = createdAt.toLocalDateTime();
    restaurantInfo.updatedAt = updatedAt.toLocalDateTime();
    restaurantInfo.isDeleted = isDeleted;
    restaurantInfo.deletedAt = toLocalDateTimeOrNull(deletedAt);
    return restaurantInfo;
  }

  // ... (필요한 비즈니스 로직 추가)
}