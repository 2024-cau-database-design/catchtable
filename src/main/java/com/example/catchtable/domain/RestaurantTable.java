package com.example.catchtable.domain;

import static com.example.catchtable.util.LocalDateTimeUtil.toLocalDateTimeOrNull;

import jakarta.validation.constraints.NotNull;
import java.sql.Timestamp;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RestaurantTable {

  @NotNull
  private Integer id;

  @NotNull
  private Integer restaurantId;

  @NotNull
  private Integer tableType;

  @NotNull
  private Integer seatCapacity;

  @NotNull
  private LocalDateTime createdAt;

  @NotNull
  private LocalDateTime updatedAt;

  @NotNull
  private Boolean isDeleted;


  private LocalDateTime deletedAt;

  // 정적 팩토리 메소드
  public static RestaurantTable create(Integer restaurantId, Integer tableType, Integer seatCapacity,
      Timestamp createdAt, Timestamp updatedAt, Boolean isDeleted, Timestamp deletedAt) {
    RestaurantTable restaurantTable = new RestaurantTable();
    restaurantTable.restaurantId = restaurantId;
    restaurantTable.tableType = tableType;
    restaurantTable.seatCapacity = seatCapacity;
    restaurantTable.createdAt = createdAt.toLocalDateTime();
    restaurantTable.updatedAt = updatedAt.toLocalDateTime();
    restaurantTable.isDeleted = isDeleted;
    restaurantTable.deletedAt = toLocalDateTimeOrNull(deletedAt);
    return restaurantTable;
  }

  // ... (필요한 비즈니스 로직 추가)
}
