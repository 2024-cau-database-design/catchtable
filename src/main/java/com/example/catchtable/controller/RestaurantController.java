package com.example.catchtable.controller;

import com.example.catchtable.domain.*;
import com.example.catchtable.repository.RestaurantRepository;
import com.example.catchtable.service.RestaurantService;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

  private final RestaurantService restaurantService;
  private final RestaurantRepository restaurantRepository;

  // 복합 조건 레스토랑 검색
  @GetMapping
  public ResponseEntity<List<Restaurant>> searchRestaurants(
      @RequestParam(required = false) String keyword,
      @RequestParam(required = false) String category) {
    List<Restaurant> restaurants = restaurantService.searchRestaurants(keyword, category);
    return ResponseEntity.ok(restaurants);
  }

  // 레스토랑 상세 정보 조회
  @GetMapping("/{restaurantId}")
  public ResponseEntity<RestaurantInfo> getRestaurantInfo(@PathVariable Long restaurantId) {
    RestaurantInfo restaurantInfo = restaurantService.getRestaurantDetails(restaurantId);
    return ResponseEntity.ok(restaurantInfo);
  }

  @GetMapping("/{restaurantId}/schedule")
  public ResponseEntity<String> getActualWorkSchedule(
      @PathVariable Long restaurantId,
      @RequestParam(value = "targetDate", required = false)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate targetDate) {
    if (targetDate == null) {
      targetDate = LocalDate.now();
    }
    String workSchedule = restaurantService.getActualWorkSchedule(restaurantId, targetDate);
    return ResponseEntity.ok(workSchedule);
  }

  // 레스토랑 등록
  @PostMapping
  public ResponseEntity<Restaurant> createRestaurant(
      @RequestParam String name,
      @RequestParam Long ownerId) {
    Restaurant restaurant = restaurantService.createRestaurant(name, ownerId);
    return ResponseEntity.status(HttpStatus.CREATED).body(restaurant);
  }

  // 레스토랑 수정
  @PutMapping("/{id}")
  public ResponseEntity<Restaurant> updateRestaurant(
      @PathVariable Long id,
      @RequestParam(required = false) String name,
      @RequestParam(required = false) Long ownerId) {

    Restaurant restaurant = restaurantRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("레스토랑을 찾을 수 없습니다."));

    if (name != null) {
      restaurant.updateName(name);
    }
    if (ownerId != null) {
      restaurant.updateOwnerId(ownerId);
    }

    restaurantRepository.save(restaurant); // 변경 감지 기능을 통해 업데이트
    return ResponseEntity.ok(restaurant);
  }

  // 레스토랑 삭제
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id) {
    restaurantService.deleteRestaurant(id);
    return ResponseEntity.noContent().build();
  }

  // 메뉴 등록
  @PostMapping("/{id}/menus")
  public ResponseEntity<RestaurantMenu> createMenu(@PathVariable Long id,
      @RequestParam String name,
      @RequestParam String description,
      @RequestParam Long price,
      @RequestParam Long foodCategoryId) {
    Optional<RestaurantMenu> menu = restaurantService.createMenu(id, name, description, price, foodCategoryId);
    if (menu.isEmpty()) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    return ResponseEntity.status(HttpStatus.CREATED).body(menu.get());
  }

  // 테이블 등록
  @PostMapping("/{id}/tables")
  public ResponseEntity<RestaurantTable> createTable(@PathVariable Long id,
      @RequestParam Integer capacity) {
    Optional<RestaurantTable> table = restaurantService.createTable(id, capacity);
    if (table.isEmpty()) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    return ResponseEntity.status(HttpStatus.CREATED).body(table.get());
  }

  // 위치 정보 등록
  @PostMapping("/{id}/locations")
  public ResponseEntity<RestaurantLocation> createLocation(@PathVariable Long id,
      @RequestParam Double latitude,
      @RequestParam Double longitude,
      @RequestParam String address) {
    Optional<RestaurantLocation> location = restaurantService.createLocation(id, latitude, longitude, address);
    if (location.isEmpty()) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    return ResponseEntity.status(HttpStatus.CREATED).body(location.get());
  }

  // 음식 카테고리 등록
  @PostMapping("/food-categories")
  public ResponseEntity<RestaurantFoodCategory> createFoodCategory(@RequestParam String name) {
    Optional<RestaurantFoodCategory> foodCategory = restaurantService.createFoodCategory(name);
    if (foodCategory.isEmpty()) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    return ResponseEntity.status(HttpStatus.CREATED).body(foodCategory.get());
  }

  // 영업 시간 등록
  @PostMapping("/{id}/work-schedules")
  public ResponseEntity<WorkSchedule> createWorkSchedule(@PathVariable Long id,
      @RequestParam String dayOfWeek,
      @RequestParam String openTime,
      @RequestParam String closeTime) {
    Optional<WorkSchedule> workSchedule = restaurantService.createWorkSchedule(id, dayOfWeek, openTime, closeTime);
    if (workSchedule.isEmpty()) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    return ResponseEntity.status(HttpStatus.CREATED).body(workSchedule.get());
  }

  // 찾는 레스토랑이 없는 예외가 발생한 경우
  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<String> restaurantNotFound() {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("레스토랑을 찾을 수 없습니다.");
  }

  // fk 제약조건에 의해 예외가 발생한 경우
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<String> foreignKeyException(RuntimeException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
  }

  // 그 외의 예외가 발생한 경우
  @ExceptionHandler
  public ResponseEntity<String> exceptionHandler(RuntimeException e) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
  }
}