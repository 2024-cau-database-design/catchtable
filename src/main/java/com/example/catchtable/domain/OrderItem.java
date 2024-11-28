package com.example.catchtable.domain;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

  private Integer id;
  private Integer orderId;
  private Integer menuId;
  private Integer quantity;
  private Integer price;


  // 정적 팩토리 메소드
  public static OrderItem fromEntity(Integer id, Integer orderId, Integer menuId,
      Integer quantity, Integer price) {
    OrderItem orderItem = new OrderItem();
    orderItem.id = id;
    orderItem.orderId = orderId;
    orderItem.menuId = menuId;
    orderItem.quantity = quantity;
    orderItem.price = price;
    return orderItem;
  }

  // ... (필요한 비즈니스 로직 추가)
}
