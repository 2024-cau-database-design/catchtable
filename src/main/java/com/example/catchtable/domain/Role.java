package com.example.catchtable.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Role {

  @NotNull
  private Integer id;

  @NotBlank
  private String roleName;

  // 정적 팩토리 메소드
  public static Role fromEntity(final Integer id, final String roleName) {
    Role role = new Role();
    role.id = id;
    role.roleName = roleName;
    return role;
  }
}