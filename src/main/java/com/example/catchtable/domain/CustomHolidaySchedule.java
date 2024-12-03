package com.example.catchtable.domain;

import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomHolidaySchedule {

  private Long id; // int unsigned -> Long
  private LocalDate date;
  private Long restaurantId; // int unsigned -> Long

  // 정적 팩토리 메소드
  public static CustomHolidaySchedule fromEntity(final Long id, final LocalDate date, final Long restaurantId) {
    CustomHolidaySchedule customHolidaySchedule = new CustomHolidaySchedule();
    customHolidaySchedule.id = id;
    customHolidaySchedule.date = date;
    customHolidaySchedule.restaurantId = restaurantId;
    return customHolidaySchedule;
  }

  // ... (필요한 비즈니스 로직 추가)
}