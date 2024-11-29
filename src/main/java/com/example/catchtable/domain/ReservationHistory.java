package com.example.catchtable.domain;

import static com.example.catchtable.util.LocalDateTimeUtil.toLocalDateTimeOrNull;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationHistory {

  private Long id; // int unsigned -> Long
  private Long statusId; // int unsigned -> Long
  private LocalDateTime visitedAt; // datetime -> LocalDateTime
  private Long reservationId; // int unsigned -> Long
  private LocalDate bookingTime; // date -> LocalDate
  private Integer guestsCount; // tinyint(6) unsigned -> Integer

  // 정적 팩토리 메소드
  public static ReservationHistory fromEntity(
      final Long id, final Long statusId, final Timestamp visitedAt, final Long reservationId,
      final LocalDate bookingTime, final Integer guestsCount) {
    ReservationHistory reservationHistory = new ReservationHistory();
    reservationHistory.id = id;
    reservationHistory.statusId = statusId;
    reservationHistory.visitedAt = toLocalDateTimeOrNull(visitedAt);
    reservationHistory.reservationId = reservationId;
    reservationHistory.bookingTime = bookingTime;
    reservationHistory.guestsCount = guestsCount;
    return reservationHistory;
  }

  // ... (필요한 비즈니스 로직 추가)
}