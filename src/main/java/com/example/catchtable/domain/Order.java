package com.example.catchtable.domain;

import static com.example.catchtable.util.LocalDateTimeUtil.toLocalDateTimeOrNull;

import jakarta.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

  @NotNull
  private Integer id;

  @NotNull
  private Integer restaurantId;

  @NotNull
  private Integer customerId;

  @NotNull
  private Integer bookingId;

  @NotNull
  private Integer statusId;

  @NotNull
  private Integer totalPrice;

  @NotNull
  private Integer reservationFee;

  @NotNull
  private LocalDateTime createdAt;

  // 정적 팩토리 메소드
  public static Order create(Integer restaurantId, Integer customerId, Integer bookingId,
      Integer reservationFee, Integer statusId, Integer totalPrice, Timestamp createdAt) {
    Order order = new Order();
    order.restaurantId = restaurantId;
    order.customerId = customerId;
    order.bookingId = bookingId;
    order.statusId = statusId;
    order.totalPrice = totalPrice;
    order.reservationFee = reservationFee;
    order.createdAt = createdAt.toLocalDateTime();
    return order;
  }

  // ... (필요한 비즈니스 로직 추가)
}