package com.example.catchtable.domain;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

  private Long id; // int unsigned -> Long
  private Long statusId; // int unsigned -> Long
  private LocalDateTime createdAt; // int unsigned -> LocalDateTime
  private Long totalPrice; // int unsigned -> Long
  private Long restaurantId; // int unsigned -> Long
  private Long customerId; // int unsigned -> Long
  private Long reservationFee; // int unsigned -> Long, nullable
  private Long bookingId; // int unsigned -> Long

  // 정적 팩토리 메소드
  public static Order fromEntity(
      final Long id, final Long statusId, final Timestamp createdAt,
      final Long totalPrice, final Long restaurantId, final Long customerId,
      final Long reservationFee, final Long bookingId) {
    Order order = new Order();
    order.id = id;
    order.statusId = statusId;
    order.createdAt = createdAt.toLocalDateTime();
    order.totalPrice = totalPrice;
    order.restaurantId = restaurantId;
    order.customerId = customerId;
    order.reservationFee = reservationFee;
    order.bookingId = bookingId;
    return order;
  }

  // ... (필요한 비즈니스 로직 추가)
}