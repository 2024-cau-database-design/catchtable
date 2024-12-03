package com.example.catchtable.domain;

import static com.example.catchtable.util.LocalDateTimeUtil.toLocalDateTimeOrNull;

import com.example.catchtable.util.LocalDateTimeUtil;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RestaurantBookmark {

  private Long id; // int unsigned -> Long
  private Long restaurantId; // int unsigned -> Long
  private Long customerId; // int unsigned -> Long
  private LocalDateTime createdAt;
  private Boolean isDeleted;
  private LocalDateTime deletedAt;

  // 정적 팩토리 메소드
  public static RestaurantBookmark fromEntity(
      final Long id, final Long restaurantId, final Long customerId,
      final Timestamp createdAt, final Boolean isDeleted, final Timestamp deletedAt) {
    RestaurantBookmark restaurantBookmark = new RestaurantBookmark();
    restaurantBookmark.id = id;
    restaurantBookmark.restaurantId = restaurantId;
    restaurantBookmark.customerId = customerId;
    restaurantBookmark.createdAt = createdAt.toLocalDateTime();
    restaurantBookmark.isDeleted = isDeleted;
    restaurantBookmark.deletedAt = toLocalDateTimeOrNull(deletedAt);
    return restaurantBookmark;
  }

  // ... (필요한 비즈니스 로직 추가)
}