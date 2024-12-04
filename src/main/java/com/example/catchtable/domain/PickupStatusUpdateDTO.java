package com.example.catchtable.domain;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PickupStatusUpdateDTO {
    @NotNull
    private String status;

    // 정적 팩토리 메소드
    public static PickupStatusUpdateDTO fromEntity(final String status) {
        PickupStatusUpdateDTO dto = new PickupStatusUpdateDTO();
        dto.status = status;
        return dto;
    }
}