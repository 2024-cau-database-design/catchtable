package com.example.catchtable.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

  private Long id; // int unsigned -> Long
  private Long quantity; // int unsigned -> Long
  private Long price; // int unsigned -> Long
  private Long orderId; // int unsigned -> Long
  private Long menuId; // int unsigned -> Long

  // 정적 팩토리 메소드
  public static OrderItem fromEntity(
      final Long id, final Long quantity, final Long price,
      final Long orderId, final Long menuId) {
    OrderItem orderItem = new OrderItem();
    orderItem.id = id;
    orderItem.quantity = quantity;
    orderItem.price = price;
    orderItem.orderId = orderId;
    orderItem.menuId = menuId;
    return orderItem;
  }

  // ... (필요한 비즈니스 로직 추가)
}