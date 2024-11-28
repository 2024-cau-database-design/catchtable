package com.example.catchtable.domain;

import static com.example.catchtable.util.LocalDateTimeUtil.toLocalDateTimeOrNull;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.sql.Timestamp;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {

  @NotNull
  private Integer id;

  @NotNull
  private Integer amount;

  @NotNull
  private Integer orderId;

  @NotBlank
  private String method;

  @NotNull
  private LocalDateTime createdAt;

  @NotNull
  private LocalDateTime updatedAt;

  @NotNull
  private Boolean isDeleted;


  private LocalDateTime deletedAt;


  // 정적 팩토리 메소드
  public static Payment fromEntity(
      final Integer id, final Integer amount, final Integer orderId,
      final Timestamp createdAt, final Timestamp updatedAt, final Boolean isDeleted, final Timestamp deletedAt, final String method) {
    Payment payment = new Payment();
    payment.id = id;
    payment.amount = amount;
    payment.orderId = orderId;
    payment.createdAt = createdAt.toLocalDateTime();
    payment.updatedAt = updatedAt.toLocalDateTime();
    payment.isDeleted = isDeleted;
    payment.deletedAt = toLocalDateTimeOrNull(deletedAt);
    payment.method = method;
    return payment;
  }
}