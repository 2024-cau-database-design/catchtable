package com.example.catchtable.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RestaurantFoodCategory {

  private Long restaurantId; // int unsigned -> Long
  private String name;

  // 정적 팩토리 메소드
  public static RestaurantFoodCategory fromEntity(final Long restaurantId, final String name) {
    RestaurantFoodCategory restaurantFoodCategory = new RestaurantFoodCategory();
    restaurantFoodCategory.restaurantId = restaurantId;
    restaurantFoodCategory.name = name;
    return restaurantFoodCategory;
  }

  // ... (필요한 비즈니스 로직 추가)
}