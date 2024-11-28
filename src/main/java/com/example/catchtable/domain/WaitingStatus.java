package com.example.catchtable.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WaitingStatus {

  @NotNull
  private Integer id;

  @NotBlank
  private String type;

  // 정적 팩토리 메소드
  public static WaitingStatus fromEntity(final Integer id, final String type) {
    WaitingStatus waitingStatus = new WaitingStatus();
    waitingStatus.id = id;
    waitingStatus.type = type;
    return waitingStatus;
  }
}