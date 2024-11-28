package com.example.catchtable.domain;

import static com.example.catchtable.util.LocalDateTimeUtil.toLocalDateTimeOrNull;

import jakarta.validation.constraints.NotNull;
import java.sql.Timestamp;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Customer {

  @NotNull
  private Integer id;

  @NotNull
  private String name;

  @NotNull
  private String phoneNumber;

  @NotNull
  private LocalDateTime createdAt;

  @NotNull
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
