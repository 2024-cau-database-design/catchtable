package com.example.catchtable.service;

import com.example.catchtable.domain.Restaurant;
import com.example.catchtable.domain.RestaurantInfo;
import com.example.catchtable.repository.RestaurantInfoRepository;
import com.example.catchtable.repository.RestaurantRepository;
import java.util.List;
import java.util.NoSuchElementException;
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

  // 복합 조건 레스토랑 검색
  @Transactional(readOnly = true)
  public List<Restaurant> searchRestaurants(String keyword, Double latitude, Double longitude, String category, String sort) {
    // 복합 조건 처리
    List<Restaurant> searchResult = restaurantRepository.searchByConditions(keyword, latitude, longitude, category, sort);
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
    Restaurant updatedRestaurant = Restaurant.builder()
        .name(name).build();

    // 저장
    return restaurantRepository.save(updatedRestaurant)
        .orElseThrow(() -> new RuntimeException("레스토랑 업데이트에 실패했습니다."));
  }


  // 레스토랑 삭제 (소프트 삭제 처리)
  public void deleteRestaurant(Long id) {
    Restaurant restaurant = restaurantRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("레스토랑을 찾을 수 없습니다."));

    Restaurant deletedRestaurant = Restaurant.builder()
        .id(id).isDeleted(true).build();

    restaurantRepository.save(deletedRestaurant);
  }

}
