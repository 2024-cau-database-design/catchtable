package com.example.catchtable.domain;

import java.sql.Time;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomWorkSchedule {

  private Long id; // int unsigned -> Long
  private LocalDate date;
  private Time startTime;
  private Time endTime;
  private Long restaurantId; // int unsigned -> Long

  // 정적 팩토리 메소드
  public static CustomWorkSchedule fromEntity(
      final Long id, final LocalDate date, final Time startTime, final Time endTime, final Long restaurantId) {
    CustomWorkSchedule customWorkSchedule = new CustomWorkSchedule();
    customWorkSchedule.id = id;
    customWorkSchedule.date = date;
    customWorkSchedule.startTime = startTime;
    customWorkSchedule.endTime = endTime;
    customWorkSchedule.restaurantId = restaurantId;
    return customWorkSchedule;
  }

  // ... (필요한 비즈니스 로직 추가)
}