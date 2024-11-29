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

  private Long id; // int unsigned -> Long
  private Long amount; // int unsigned -> Long
  private Long orderId; // int unsigned -> Long
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Boolean isDeleted;
  private LocalDateTime deletedAt;
  private String method;

  // 정적 팩토리 메소드
  public static Payment fromEntity(
      final Long id, final Long amount, final Long orderId, // int unsigned -> Long
      final Timestamp createdAt, final Timestamp updatedAt,
      final Boolean isDeleted, final Timestamp deletedAt, final String method) {
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