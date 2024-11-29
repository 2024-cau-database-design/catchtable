package com.example.catchtable.domain;

import jakarta.validation.constraints.NotNull;
import java.sql.Date;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentHistory {

  private Long id; // int unsigned -> Long
  private String method;
  private Long amount; // int unsigned -> Long
  private Long status; // int unsigned -> Long
  private LocalDate transactionDate;
  private Long paymentId; // int unsigned -> Long

  // 정적 팩토리 메소드
  public static PaymentHistory fromEntity(
      final Long id, final String method, final Long amount, final Long status, // int unsigned -> Long
      final LocalDate transactionDate, final Long paymentId) {
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