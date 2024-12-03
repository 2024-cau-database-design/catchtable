package com.example.catchtable.domain;

import static com.example.catchtable.util.LocalDateTimeUtil.toLocalDateTimeOrNull;

import jakarta.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationHistory {

  private Long id;
  private Integer statusId;
  private LocalDateTime visitedAt;
  private Integer reservationId;
  private LocalDate bookingTime;
  private Integer guestsCount;

  // 정적 팩토리 메소드
  public static ReservationHistory fromEntity(
      final Long id, final Integer statusId, final Timestamp visitedAt, final Integer reservationId, final LocalDate bookingTime, final Integer guestsCount) {
    ReservationHistory reservationHistory = new ReservationHistory();
    reservationHistory.id = id;
    reservationHistory.statusId = statusId;
    reservationHistory.visitedAt = toLocalDateTimeOrNull(visitedAt);
    reservationHistory.reservationId = reservationId;
    reservationHistory.bookingTime = bookingTime;
    reservationHistory.guestsCount = guestsCount;
    return reservationHistory;
  }
}