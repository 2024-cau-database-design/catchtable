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
public class RestaurantPhoto {

  @NotNull
  private Integer id;

  @NotNull
  private Integer restaurantId;


  // private String photoName;
  private String photoPath;
  private String photoType;
  private Integer photoSize;
  private String description;
  private LocalDateTime createdAt;
  private Boolean isDeleted;
  private LocalDateTime deletedAt;

  // 정적 팩토리 메소드
  public static RestaurantPhoto fromEntity(Integer restaurantId, String photoPath,
      String photoType, Integer photoSize, String description,
      Timestamp createdAt, Boolean isDeleted, Timestamp deletedAt) {
    RestaurantPhoto restaurantPhoto = new RestaurantPhoto();
    restaurantPhoto.restaurantId = restaurantId;
    // restaurantPhoto.photoName = photoName;
    restaurantPhoto.photoPath = photoPath;
    restaurantPhoto.photoType = photoType;
    restaurantPhoto.photoSize = photoSize;
    restaurantPhoto.description = description;
    restaurantPhoto.createdAt = createdAt.toLocalDateTime();
    restaurantPhoto.isDeleted = isDeleted;
    restaurantPhoto.deletedAt = toLocalDateTimeOrNull(deletedAt);
    return restaurantPhoto;
  }

  // ... (필요한 비즈니스 로직 추가)
}
