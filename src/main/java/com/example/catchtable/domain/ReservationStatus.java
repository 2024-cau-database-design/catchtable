package com.example.catchtable.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationStatus {

  @NotNull
  private Integer id;

  @NotBlank
  private String type;

  // 정적 팩토리 메소드
  public static ReservationStatus fromEntity(final Integer id, final String type) {
    ReservationStatus reservationStatus = new ReservationStatus();
    reservationStatus.id = id;
    reservationStatus.type = type;
    return reservationStatus;
  }
}