package com.example.catchtable.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderStatus {

  private Integer id;
  private String type;

  // 정적 팩토리 메소드
  public static OrderStatus create(String type) {
    OrderStatus orderStatus = new OrderStatus();
    orderStatus.type = type;
    return orderStatus;
  }

  // ... (필요한 비즈니스 로직 추가)
}
