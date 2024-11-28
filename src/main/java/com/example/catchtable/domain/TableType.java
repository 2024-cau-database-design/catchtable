package com.example.catchtable.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TableType {

  @NotNull
  private Integer id;

  @NotBlank
  private String typeName;

  // 정적 팩토리 메소드
  public static TableType create(String typeName) {
    TableType tableType = new TableType();
    tableType.typeName = typeName;
    return tableType;
  }

  // ... (필요한 비즈니스 로직 추가)
}