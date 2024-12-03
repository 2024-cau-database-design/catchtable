package com.example.catchtable.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RestaurantImage {

  private Long id; // int unsigned -> Long
  private Long restaurantId; // int unsigned -> Long
  private String name;
  private String url;
  private String description;

  // 정적 팩토리 메소드
  public static RestaurantImage fromEntity(
      final Long id, final Long restaurantId, final String name, final String url, final String description) {
    RestaurantImage restaurantImage = new RestaurantImage();
    restaurantImage.id = id;
    restaurantImage.restaurantId = restaurantId;
    restaurantImage.name = name;
    restaurantImage.url = url;
    restaurantImage.description = description;
    return restaurantImage;
  }

  // ... (필요한 비즈니스 로직 추가)
}