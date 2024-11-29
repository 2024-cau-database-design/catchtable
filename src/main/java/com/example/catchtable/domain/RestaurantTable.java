package com.example.catchtable.domain;

import static com.example.catchtable.util.LocalDateTimeUtil.toLocalDateTimeOrNull;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RestaurantTable {

  private Long id; // int unsigned -> Long
  private Long restaurantId; // int unsigned -> Long
  private Long tableTypeId; // int unsigned -> Long, tableType -> tableTypeId
  private Integer seatCapacity;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Boolean isDeleted;
  private LocalDateTime deletedAt;

  // 정적 팩토리 메소드
  public static RestaurantTable fromEntity(
      final Long id, final Long restaurantId, final Long tableTypeId, final Integer seatCapacity, // 타입 변경 및 필드명 수정
      final Timestamp createdAt, final Timestamp updatedAt, final Boolean isDeleted, final Timestamp deletedAt) {
    RestaurantTable restaurantTable = new RestaurantTable();
    restaurantTable.id = id;
    restaurantTable.restaurantId = restaurantId;
    restaurantTable.tableTypeId = tableTypeId; // 필드명 수정
    restaurantTable.seatCapacity = seatCapacity;
    restaurantTable.createdAt = createdAt.toLocalDateTime();
    restaurantTable.updatedAt = updatedAt.toLocalDateTime();
    restaurantTable.isDeleted = isDeleted;
    restaurantTable.deletedAt = toLocalDateTimeOrNull(deletedAt);
    return restaurantTable;
  }

  // ... (필요한 비즈니스 로직 추가)
}