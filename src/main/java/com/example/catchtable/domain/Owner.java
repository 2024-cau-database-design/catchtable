package com.example.catchtable.domain;

import static com.example.catchtable.util.LocalDateTimeUtil.toLocalDateTimeOrNull;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Owner {

  private Long id; // int unsigned -> Long
  private LocalDateTime createdAt;
  private Boolean isDeleted;
  private LocalDateTime deletedAt;

  // 정적 팩토리 메소드
  public static Owner fromEntity(
      final Long id, // int unsigned -> Long
      final Timestamp createdAt,
      final Boolean isDeleted, final Timestamp deletedAt) {
    Owner owner = new Owner();
    owner.id = id;
    owner.createdAt = createdAt.toLocalDateTime();
    owner.isDeleted = isDeleted;
    owner.deletedAt = toLocalDateTimeOrNull(deletedAt);
    return owner;
  }

  // ... (필요한 비즈니스 로직 추가)
}