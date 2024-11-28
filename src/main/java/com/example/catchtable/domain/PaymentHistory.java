package com.example.catchtable.domain;

import jakarta.validation.constraints.NotNull;
import java.sql.Date;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentHistory {

  @NotNull
  private Integer id;

  @NotNull
  private Integer method;

  @NotNull
  private Integer amount;

  @NotNull
  private Integer status;

  @NotNull
  private Date transactionDate;

  @NotNull
  private Integer paymentId;

  // 정적 팩토리 메소드
  public static PaymentHistory fromEntity(
      final Integer id, final Integer method, final Integer amount, final Integer status,
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