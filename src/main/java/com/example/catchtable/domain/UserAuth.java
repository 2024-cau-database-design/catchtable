package com.example.catchtable.domain;

import static com.example.catchtable.util.LocalDateTimeUtil.toLocalDateTimeOrNull;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserAuth {

  private Long id; // int unsigned -> Long
  private String passwordHash;
  private String loginId; // ident -> loginId
  private String email;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Boolean isDeleted;
  private LocalDateTime deletedAt;

  // 정적 팩토리 메소드
  public static UserAuth fromEntity(
      final Long id, final String passwordHash, // int unsigned -> Long
      final String loginId, final String email, // ident -> loginId
      final Timestamp createdAt, final Timestamp updatedAt,
      final Boolean isDeleted, final Timestamp deletedAt) {
    UserAuth userAuth = new UserAuth();
    userAuth.id = id;
    userAuth.passwordHash = passwordHash;
    userAuth.loginId = loginId; // ident -> loginId
    userAuth.email = email;
    userAuth.createdAt = createdAt.toLocalDateTime();
    userAuth.updatedAt = updatedAt.toLocalDateTime();
    userAuth.isDeleted = isDeleted;
    userAuth.deletedAt = toLocalDateTimeOrNull(deletedAt);
    return userAuth;
  }

  // ... (필요한 비즈니스 로직 추가)
}