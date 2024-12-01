package com.example.catchtable.domain;

import static com.example.catchtable.util.LocalDateTimeUtil.toLocalDateTimeOrNull;

import jakarta.validation.constraints.NotNull;
import java.sql.Timestamp;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PickupCreateRequestDTO {

    @NotNull
    private Long userId; // 사용자 ID

    @NotNull
    private Long restaurantId; // 레스토랑 ID

    @NotNull
    private LocalDateTime pickupAt; // 픽업 날짜

    @NotNull
    private List<PickupMenu> pickupMenus; // 픽업 메뉴 목록

    // 정적 팩토리 메소드
    public static PickupCreateRequestDTO fromEntity(
            final Long userId,
            final Long restaurantId,
            final LocalDateTime pickupAt,
            final List<PickupMenu> pickupMenus
    ) {
        PickupCreateRequestDTO dto = new PickupCreateRequestDTO();
        dto.userId = userId;
        dto.restaurantId = restaurantId;
        dto.pickupAt = pickupAt;
        dto.pickupMenus = pickupMenus;
        return dto;
    }

    // 내부 클래스: PickupMenu
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PickupMenu {

        @NotNull
        private Long menuId; // 메뉴 ID

        @NotNull
        private Integer quantity; // 메뉴 수량

        // 정적 팩토리 메소드
        public static PickupMenu fromEntity(final Long menuId, final Integer quantity) {
            PickupMenu menu = new PickupMenu();
            menu.menuId = menuId;
            menu.quantity = quantity;
            return menu;
        }
    }
}