package com.example.catchtable.domain;

import static com.example.catchtable.util.LocalDateTimeUtil.toLocalDateTimeOrNull;

import jakarta.validation.constraints.NotNull;
import java.sql.Timestamp;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuImage {

  @NotNull
  private Integer id;

  @NotNull
  private String name;

  @NotNull
  private String url;

  @NotNull
  private Integer menuId; // FK 확인 필요: review_id? menu_id?

  @NotNull
  private LocalDateTime createdAt;

  @NotNull
  private Boolean isDeleted;


  private LocalDateTime deletedAt;

  // 정적 팩토리 메소드
  public static MenuImage fromEntity(
      final Integer id, final String name, final String url, final Integer menuId,
      final Timestamp createdAt, final Boolean isDeleted, final Timestamp deletedAt) {
    MenuImage menuImage = new MenuImage();
    menuImage.id = id;
    menuImage.name = name;
    menuImage.url = url;
    menuImage.menuId = menuId;
    menuImage.createdAt = createdAt.toLocalDateTime();
    menuImage.isDeleted = isDeleted;
    menuImage.deletedAt = toLocalDateTimeOrNull(deletedAt);
    return menuImage;
  }
}