package com.example.catchtable.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderStatus {

  private Long id; // int unsigned -> Long
  private String type;

  // 정적 팩토리 메소드
  public static OrderStatus fromEntity(Long id, String type) {
    OrderStatus orderStatus = new OrderStatus();
    orderStatus.id = id;
    orderStatus.type = type;
    return orderStatus;
  }

  // ... (필요한 비즈니스 로직 추가)
}
