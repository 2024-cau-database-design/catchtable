package com.example.catchtable.domain;

import jakarta.validation.constraints.NotNull;
import java.sql.Date;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentHistory {

  private Long id;
  private Integer method;
  private Integer amount;
  private Integer status;
  private Date transactionDate;
  private Integer paymentId;

  // 정적 팩토리 메소드
  public static PaymentHistory fromEntity(
      final Long id, final Integer method, final Integer amount, final Integer status,
      final Date transactionDate, final Integer paymentId) {
    PaymentHistory paymentHistory = new PaymentHistory();
    paymentHistory.id = id;
    paymentHistory.method = method;
    paymentHistory.amount = amount;
    paymentHistory.status = status;
    paymentHistory.transactionDate = transactionDate;
    paymentHistory.paymentId = paymentId;
    return paymentHistory;
  }
}