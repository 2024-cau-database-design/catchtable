package com.example.catchtable.domain;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE) // @Builder와 함께 사용
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자
public class Waiting {

  private Long id; // int unsigned -> Long
  private LocalDateTime createdAt; // int -> Long
  private Long customerId; // int unsigned -> Long
  private Integer guestCount;
  private Long restaurantId; // int unsigned -> Long

  // 정적 팩토리 메소드
  public static Waiting fromEntity(
      final Long id, final Timestamp createdAt, final Long customerId, final Integer guestCount, final Long restaurantId) {
    Waiting waiting = new Waiting();
    waiting.id = id;
    waiting.createdAt = createdAt.toLocalDateTime();
    waiting.customerId = customerId;
    waiting.guestCount = guestCount;
    waiting.restaurantId = restaurantId;
    return waiting;
  }

  // ... (필요한 비즈니스 로직 추가)
}