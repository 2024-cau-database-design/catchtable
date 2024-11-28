package com.example.catchtable.domain;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

  @NotNull
  private Integer id;

  @NotNull
  private Integer orderId;

  @NotNull
  private Integer menuId;

  @NotNull
  private Integer quantity;

  @NotNull
  private Integer price;


  // 정적 팩토리 메소드
  public static OrderItem create(Integer orderId, Integer menuId, Integer quantity, Integer price) {
    OrderItem orderItem = new OrderItem();
    orderItem.orderId = orderId;
    orderItem.menuId = menuId;
    orderItem.quantity = quantity;
    orderItem.price = price;
    return orderItem;
  }

  // ... (필요한 비즈니스 로직 추가)
}
