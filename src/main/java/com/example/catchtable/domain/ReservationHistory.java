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

  @NotNull
  private Integer id;

  @NotNull
  private Integer statusId;


  private LocalDateTime visitedAt;

  @NotNull
  private Integer reservationId;

  @NotNull
  private LocalDate bookingTime;

  @NotNull
  private Integer guestsCount;

  // 정적 팩토리 메소드
  public static ReservationHistory fromEntity(
      final Integer id, final Integer statusId, final Timestamp visitedAt, final Integer reservationId, final LocalDate bookingTime, final Integer guestsCount) {
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