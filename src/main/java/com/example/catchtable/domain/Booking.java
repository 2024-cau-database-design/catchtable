package com.example.catchtable.domain;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Booking {

  private Integer id;
  private String type;

  // 정적 팩토리 메소드
  public static Booking fromEntity(String type) {
    Booking booking = new Booking();
    booking.type = type;
    return booking;
  }

  // ... (필요한 비즈니스 로직 추가)
}
