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

  @NotNull
  private Integer id;

  @NotNull
  private Integer restaurantId;

  @NotBlank
  private String menuName;


  private String menuDescription;

  @NotNull
  private Integer menuPrice;


  // private String menuPhotoPath;

  @NotNull
  private LocalDateTime createdAt;

  @NotNull
  private LocalDateTime updatedAt;

  // 정적 팩토리 메소드
  public static RestaurantMenu create(Integer restaurantId, String menuName, String menuDescription, Integer menuPrice,
      Timestamp createdAt, Timestamp updatedAt) {
    RestaurantMenu restaurantMenu = new RestaurantMenu();
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
