package com.example.catchtable.domain;

import static com.example.catchtable.util.LocalDateTimeUtil.toLocalDateTimeOrNull;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.sql.Timestamp;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import lombok.Setter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserAuth {

  @Setter
  @NotNull
  private Integer id;

  @NotBlank
  private String passwordHash;

  @NotBlank
  private String ident;

  @NotBlank
  private String email;

  @NotNull
  private LocalDateTime createdAt;

  @NotNull
  private LocalDateTime updatedAt;

  @NotNull
  private Boolean isDeleted;


  private LocalDateTime deletedAt;

  // 정적 팩토리 메소드
  public static UserAuth fromEntity(
      final Integer id, final String passwordHash,
      final String ident, final String email,
      final Timestamp createdAt, final Timestamp updatedAt,
      final Boolean isDeleted, final Timestamp deletedAt) {
    UserAuth userAuth = new UserAuth();
    userAuth.id = id;
    userAuth.passwordHash = passwordHash;
    userAuth.ident = ident;
    userAuth.email = email;
    userAuth.createdAt = createdAt.toLocalDateTime();
    userAuth.updatedAt = updatedAt.toLocalDateTime();
    userAuth.isDeleted = isDeleted;
    userAuth.deletedAt = toLocalDateTimeOrNull(deletedAt);
    return userAuth;
  }

  // ... (필요한 비즈니스 로직 추가)
}
