package com.example.catchtable.domain;

import static com.example.catchtable.util.LocalDateTimeUtil.toLocalDateTimeOrNull;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Customer {

  private Long id; // int unsigned -> Long
  private String name;
  private String phoneNumber;

  // 정적 팩토리 메소드
  public static Customer fromEntity(
      final Long id, final String name, // int unsigned -> Long
      final String phoneNumber) {
    Customer customer = new Customer();
    customer.id = id;
    customer.name = name;
    customer.phoneNumber = phoneNumber;
    return customer;
  }

  // ... (필요한 비즈니스 로직 추가)
}
