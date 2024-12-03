package com.example.catchtable.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TableType {

  private Long id; // int unsigned -> Long
  private String typeName;

  // 정적 팩토리 메소드
  public static TableType fromEntity(Long id, String typeName) { // int unsigned -> Long
    TableType tableType = new TableType();
    tableType.id = id;
    tableType.typeName = typeName;
    return tableType;
  }

  // ... (필요한 비즈니스 로직 추가)
}