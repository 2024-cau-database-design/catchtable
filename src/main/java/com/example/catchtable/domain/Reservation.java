package com.example.catchtable.domain;

import static com.example.catchtable.util.LocalDateTimeUtil.toLocalDateTimeOrNull;

import jakarta.validation.constraints.NotNull;
import java.sql.Timestamp;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation {

  private Long id; // int unsigned -> Long
  private Long reservationTimeId; // int unsigned -> Long
  private LocalDate bookingDate;
  private Integer guestsCount; // tinyint(6) unsigned -> Integer
  private Long restaurantTableId; // int unsigned -> Long
  private Boolean isHidden; // tinyint(1) -> Boolean
  private Long restaurantId; // int unsigned -> Long
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Boolean isDeleted;
  private LocalDateTime deletedAt;

  // 정적 팩토리 메소드
  public static Reservation fromEntity(
      final Long id, final Long reservationTimeId, final LocalDate bookingDate, // 타입 변경
      final Integer guestsCount, final Long restaurantTableId, // 타입 변경
      final Boolean isHidden, final Long restaurantId, // 필드 추가
      final Timestamp createdAt, final Timestamp updatedAt,
      final Boolean isDeleted, final Timestamp deletedAt) {
    Reservation reservation = new Reservation();
    reservation.id = id;
    reservation.reservationTimeId = reservationTimeId;
    reservation.bookingDate = bookingDate;
    reservation.guestsCount = guestsCount;
    reservation.restaurantTableId = restaurantTableId;
    reservation.isHidden = isHidden;
    reservation.restaurantId = restaurantId;
    reservation.createdAt = createdAt.toLocalDateTime();
    reservation.updatedAt = updatedAt.toLocalDateTime();
    reservation.isDeleted = isDeleted;
    reservation.deletedAt = toLocalDateTimeOrNull(deletedAt);

    return reservation;
  }

  // ... (필요한 비즈니스 로직 추가)
}
