package com.example.catchtable.domain;

import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RestaurantLocation {

  private Long restaurantId; // int unsigned -> Long
  private BigDecimal latitude;
  private BigDecimal longitude;

  // 정적 팩토리 메소드
  public static RestaurantLocation fromEntity(
      final Long restaurantId, // int unsigned -> Long
      final BigDecimal latitude, final BigDecimal longitude) {
    RestaurantLocation restaurantLocation = new RestaurantLocation();
    restaurantLocation.restaurantId = restaurantId;
    restaurantLocation.latitude = latitude;
    restaurantLocation.longitude = longitude;
    return restaurantLocation;
  }

  // ... (필요한 비즈니스 로직 추가)
}
