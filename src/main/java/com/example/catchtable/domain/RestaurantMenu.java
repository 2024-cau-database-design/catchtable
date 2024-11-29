package com.example.catchtable.domain;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RestaurantMenu {

  private Long id; // int unsigned -> Long
  private Long restaurantId; // int unsigned -> Long
  private String name;
  private String description;
  private Long price; // int unsigned -> Long
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Boolean isHidden; // tinyint(1) -> Boolean

  // 정적 팩토리 메소드
  public static RestaurantMenu fromEntity(
      final Long id, final Long restaurantId, final String name,
      final String description, final Long price, final Timestamp createdAt,
      final Timestamp updatedAt, final Boolean isHidden) {
    RestaurantMenu restaurantMenu = new RestaurantMenu();
    restaurantMenu.id = id;
    restaurantMenu.restaurantId = restaurantId;
    restaurantMenu.name = name;
    restaurantMenu.description = description;
    restaurantMenu.price = price;
    restaurantMenu.createdAt = createdAt.toLocalDateTime();
    restaurantMenu.updatedAt = updatedAt.toLocalDateTime();
    restaurantMenu.isHidden = isHidden;
    return restaurantMenu;
  }

  // ... (필요한 비즈니스 로직 추가)
}