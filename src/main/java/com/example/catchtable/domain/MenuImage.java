package com.example.catchtable.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuImage {

  private Long id; // int unsigned -> Long
  private String name;
  private String url;
  private Long menuId; // int unsigned -> Long, review_id -> menuId

  // 정적 팩토리 메소드
  public static MenuImage fromEntity(
      final Long id, final String name, final String url, final Long menuId) {
    MenuImage menuImage = new MenuImage();
    menuImage.id = id;
    menuImage.name = name;
    menuImage.url = url;
    menuImage.menuId = menuId;
    return menuImage;
  }

  // ... (필요한 비즈니스 로직 추가)
}