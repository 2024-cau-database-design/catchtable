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

  private Integer id;
  private Integer reservationTimeId;
  private LocalDate bookingDate;
  private Byte guestsCount;
  private Integer restaurantTableId;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Boolean isDeleted;
  private LocalDateTime deletedAt;



  // 정적 팩토리 메소드
  public static Reservation create(Integer reservationTimeId, LocalDate bookingDate,
      Byte guestsCount, Integer restaurantTableId,
      Timestamp createdAt, Timestamp updatedAt, Boolean isDeleted, Timestamp deletedAt) {
    Reservation reservation = new Reservation();
    reservation.reservationTimeId = reservationTimeId;
    reservation.bookingDate = bookingDate;
    reservation.guestsCount = guestsCount;
    reservation.restaurantTableId = restaurantTableId;
    reservation.createdAt = createdAt.toLocalDateTime();
    reservation.updatedAt = updatedAt.toLocalDateTime();
    reservation.isDeleted = isDeleted;
    reservation.deletedAt = toLocalDateTimeOrNull(deletedAt);

    return reservation;
  }

  // ... (필요한 비즈니스 로직 추가)
}
