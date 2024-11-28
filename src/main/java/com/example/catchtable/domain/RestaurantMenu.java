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
public class RestaurantMenu {

  private Integer id;
  private Integer restaurantId;
  private String menuName;
  private String menuDescription;
  private Integer menuPrice;
  // private String menuPhotoPath;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  // 정적 팩토리 메소드
  public static RestaurantMenu fromEntity(Integer id, Integer restaurantId,
      String menuName, String menuDescription, Integer menuPrice,
      Timestamp createdAt, Timestamp updatedAt) {
    RestaurantMenu restaurantMenu = new RestaurantMenu();
    restaurantMenu.id = id;
    restaurantMenu.restaurantId = restaurantId;
    restaurantMenu.menuName = menuName;
    restaurantMenu.menuDescription = menuDescription;
    restaurantMenu.menuPrice = menuPrice;
    // restaurantMenu.menuPhotoPath = menuPhotoPath;
    restaurantMenu.createdAt = createdAt.toLocalDateTime();
    restaurantMenu.updatedAt = updatedAt.toLocalDateTime();

    return restaurantMenu;
  }

  // ... (필요한 비즈니스 로직 추가)
}
