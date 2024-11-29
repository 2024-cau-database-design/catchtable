package com.example.catchtable.domain;

import jakarta.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

  private Integer id;
  private Integer restaurantId;
  private Integer customerId;
  private Integer bookingId;
  private Integer statusId;
  private Integer totalPrice;
  private Integer reservationFee;
  private LocalDateTime createdAt;

  // 정적 팩토리 메소드
  public static Order fromEntity(
          Integer id,
          Integer restaurantId,
          Integer customerId,
          Integer bookingId,
          Integer reservationFee,
          Integer statusId,
          Integer totalPrice,
          Timestamp createdAt
  ) {
    Order order = new Order();
    order.id = id;
    order.restaurantId = restaurantId;
    order.customerId = customerId;
    order.bookingId = bookingId;
    order.reservationFee = reservationFee;
    order.statusId = statusId;
    order.totalPrice = totalPrice;
    order.createdAt = createdAt.toLocalDateTime();
    return order;
  }

  // ... (필요한 비즈니스 로직 추가)
}