package com.example.catchtable.domain;

import java.sql.Time;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkSchedule {

  private Long id; // int unsigned -> Long
  private String dayOfWeek;
  private Time startTime;
  private Time endTime;
  private Long restaurantId; // int unsigned -> Long

  // 정적 팩토리 메소드
  public static WorkSchedule fromEntity(
      final Long id, final String dayOfWeek, final Time startTime, final Time endTime, final Long restaurantId) {
    WorkSchedule workSchedule = new WorkSchedule();
    workSchedule.id = id;
    workSchedule.dayOfWeek = dayOfWeek;
    workSchedule.startTime = startTime;
    workSchedule.endTime = endTime;
    workSchedule.restaurantId = restaurantId;
    return workSchedule;
  }

  // ... (필요한 비즈니스 로직 추가)
}