package com.example.catchtable.domain;

import static com.example.catchtable.util.LocalDateTimeUtil.toLocalDateTimeOrNull;

import jakarta.validation.constraints.NotNull;
import java.sql.Timestamp;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import lombok.Setter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Customer {

  private Integer id;
  private String name;
  private String phoneNumber;
  private LocalDateTime createdAt;
  private Boolean isDeleted;
  private LocalDateTime deletedAt;

  // 정적 팩토리 메소드
  public static Customer fromEntity(
      final Integer id, final String name,
      final String phoneNumber, final Timestamp createdAt,
      final Boolean isDeleted, final Timestamp deletedAt) {
    Customer customer = new Customer();
    customer.id = id;
    customer.name = name;
    customer.phoneNumber = phoneNumber;
    customer.createdAt = createdAt.toLocalDateTime();
    customer.isDeleted = isDeleted;
    customer.deletedAt = toLocalDateTimeOrNull(deletedAt);
    return customer;
  }

  // ... (필요한 비즈니스 로직 추가)
}
