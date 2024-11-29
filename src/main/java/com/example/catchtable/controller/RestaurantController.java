package com.example.catchtable.controller;

import com.example.catchtable.domain.Restaurant;
import com.example.catchtable.domain.RestaurantInfo;
import com.example.catchtable.service.RestaurantService;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

  private final RestaurantService restaurantService;

  // 복합 조건 레스토랑 검색
  @GetMapping
  public ResponseEntity<List<Restaurant>> searchRestaurants(
      @RequestParam(required = false) String keyword,
      @RequestParam(required = false) Double latitude,
      @RequestParam(required = false) Double longitude,
      @RequestParam(required = false) String category,
      @RequestParam(defaultValue = "name") String sort) {
    List<Restaurant> restaurants = restaurantService.searchRestaurants(keyword, latitude, longitude, category, sort);
    return ResponseEntity.ok(restaurants);
  }

  // 레스토랑 상세 정보 조회
  @GetMapping("/{id}")
  public ResponseEntity<RestaurantInfo> getRestaurantInfo(@PathVariable Long id) {
    RestaurantInfo restaurantInfo = restaurantService.getRestaurantDetails(id);
    return ResponseEntity.ok(restaurantInfo);
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
      @RequestParam String name) {
    Restaurant updatedRestaurant = restaurantService.updateRestaurant(id, name);
    return ResponseEntity.ok(updatedRestaurant);
  }

  // 레스토랑 삭제
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id) {
    restaurantService.deleteRestaurant(id);
    return ResponseEntity.noContent().build();
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
