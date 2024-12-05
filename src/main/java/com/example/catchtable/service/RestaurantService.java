package com.example.catchtable.service;

import com.example.catchtable.domain.*;
import com.example.catchtable.repository.*;
import java.math.BigDecimal;
import java.sql.Time;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantService {

  private final RestaurantRepository restaurantRepository;
  private final RestaurantInfoRepository restaurantInfoRepository;
  private final RestaurantMenuRepository menuRepository;
  private final RestaurantTableRepository tableRepository;
  private final RestaurantLocationRepository locationRepository;
  private final RestaurantFoodCategoryRepository foodCategoryRepository;
  private final WorkScheduleRepository workScheduleRepository;

  // 복합 조건 레스토랑 검색
  @Transactional(readOnly = true)
  public List<Restaurant> searchRestaurants(String keyword, String category) {
    // 복합 조건 처리
    List<Restaurant> searchResult = restaurantRepository.searchByConditions(keyword, category);
    if (searchResult.isEmpty()) {
      throw new NoSuchElementException();
    }
    return searchResult;
  }

  // 레스토랑 상세 정보 조회
  @Transactional(readOnly = true)
  public RestaurantInfo getRestaurantDetails(Long id) {
    return restaurantInfoRepository.findById(id)
        .orElseThrow(NoSuchElementException::new);
  }

  // 레스토랑 특정 날짜 운영시간 조회
  public String getActualWorkSchedule(Long restaurantId, LocalDate targetDate) {
    return restaurantRepository.getActualWorkSchedule(restaurantId, targetDate);
  }

  // 레스토랑 등록
  public Restaurant createRestaurant(String name, Long ownerId) {
    Restaurant newRestaurant = Restaurant.builder()
        .name(name).ownerId(ownerId).build();

    return restaurantRepository.save(newRestaurant)
        .orElseThrow(() -> new DataIntegrityViolationException("점주를 찾을 수 없습니다."));
  }

  // 레스토랑 수정
  public Restaurant updateRestaurant(Long id, String name) {
    // 기존 레스토랑 조회
    Restaurant restaurant = restaurantRepository.findById(id)
        .orElseThrow(NoSuchElementException::new);

    // 업데이트 처리
    restaurant.updateName(name); // Restaurant 엔티티에 updateName 메서드 추가

    // 저장 (save 메서드는 변경 감지 기능을 통해 업데이트 수행)
    return restaurantRepository.save(restaurant)
        .orElseThrow(() -> new RuntimeException("레스토랑 업데이트에 실패했습니다."));
  }


  // 레스토랑 삭제 (소프트 삭제 처리)
  public void deleteRestaurant(Long id) {
    Restaurant restaurant = restaurantRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("레스토랑을 찾을 수 없습니다."));

    restaurant.delete(); // Restaurant 엔티티에 delete 메서드 추가 (isDeleted = true 설정)

    restaurantRepository.save(restaurant);
  }

  // 메뉴 등록
  public Optional<RestaurantMenu> createMenu(Long restaurantId, String name, String description, Long price, Long foodCategoryId) {
    Restaurant restaurant = restaurantRepository.findById(restaurantId)
        .orElseThrow(() -> new NoSuchElementException("레스토랑을 찾을 수 없습니다."));

    RestaurantMenu menu = RestaurantMenu.builder()
        .restaurantId(restaurant.getId())
        .name(name)
        .description(description)
        .price(price)
        .build();

    return menuRepository.save(menu);
  }

  // 테이블 등록
  public Optional<RestaurantTable> createTable(Long restaurantId, Integer capacity) {
    Restaurant restaurant = restaurantRepository.findById(restaurantId)
        .orElseThrow(() -> new NoSuchElementException("레스토랑을 찾을 수 없습니다."));

    RestaurantTable table = RestaurantTable.builder()
        .restaurantId(restaurant.getId())
        .seatCapacity(capacity)
        .build();

    return tableRepository.save(table);
  }

  // 위치 정보 등록
  public Optional<RestaurantLocation> createLocation(Long restaurantId, Double latitude, Double longitude, String address) {
    Restaurant restaurant = restaurantRepository.findById(restaurantId)
        .orElseThrow(() -> new NoSuchElementException("레스토랑을 찾을 수 없습니다."));

    RestaurantLocation location = RestaurantLocation.builder()
        .restaurantId(restaurant.getId())
        .latitude(BigDecimal.valueOf(latitude))
        .longitude(BigDecimal.valueOf(longitude))
        .build();

    return locationRepository.save(location);
  }

  // 음식 카테고리 등록
  public Optional<RestaurantFoodCategory> createFoodCategory(String name) {
    RestaurantFoodCategory foodCategory = RestaurantFoodCategory.builder()
        .name(name)
        .build();

    return foodCategoryRepository.save(foodCategory);
  }

  // 영업 시간 등록
  public Optional<WorkSchedule> createWorkSchedule(Long restaurantId, String dayOfWeek, String startTime, String endTime) {
    Restaurant restaurant = restaurantRepository.findById(restaurantId)
        .orElseThrow(() -> new NoSuchElementException("레스토랑을 찾을 수 없습니다."));

    WorkSchedule workSchedule = WorkSchedule.builder()
        .restaurantId(restaurant.getId())
        .dayOfWeek(dayOfWeek)
        .startTime(Time.valueOf(startTime))
        .endTime(Time.valueOf(endTime))
        .build();

    return workScheduleRepository.save(workSchedule);
  }
}