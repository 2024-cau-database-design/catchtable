package com.example.catchtable.domain;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentHistory {

  private Long id;
  private String method;
  private Integer amount;
  private Integer statusId;
  private LocalDateTime transactionAt;
  private Integer paymentId;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  // 정적 팩토리 메소드
  public static PaymentHistory fromEntity(
          final Long id, final String method, final Integer amount, final Integer statusId,
          final Integer paymentId, final LocalDateTime transactionAt,
          final Timestamp createdAt, final Timestamp updatedAt) {
    PaymentHistory paymentHistory = new PaymentHistory();
    paymentHistory.id = id;
    paymentHistory.method = method;
    paymentHistory.amount = amount;
    paymentHistory.statusId = statusId;
    paymentHistory.transactionAt = transactionAt;
    paymentHistory.paymentId = paymentId;
    paymentHistory.createdAt = createdAt.toLocalDateTime();
    paymentHistory.updatedAt = updatedAt.toLocalDateTime();
    return paymentHistory;
  }
}