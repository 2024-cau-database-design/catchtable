package com.example.catchtable.service;

import com.example.catchtable.domain.Waiting;
import com.example.catchtable.domain.WaitingHistory;
import com.example.catchtable.repository.WaitingRepository;
import com.example.catchtable.repository.WaitingHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WaitingService {

  private final WaitingRepository waitingRepository;
  private final WaitingHistoryRepository waitingHistoryRepository;

  @Transactional
  public Waiting createWaiting(Long customerId, Integer guestCount, Long restaurantId) {
      // 1. Waiting 객체 생성
      Waiting newWaiting = Waiting.builder()
          .createdAt(LocalDateTime.now()) // 현재 시간 설정
          .customerId(customerId)
          .guestCount(guestCount)
          .restaurantId(restaurantId)
          .build();

      // 2. Optional로 반환된 결과 처리
      Waiting savedWaiting = waitingRepository.save(newWaiting)
          .orElseThrow(() -> new IllegalStateException("Failed to save Waiting entity"));

      // 3. WaitingHistory 생성 및 저장
      WaitingHistory newHistory = WaitingHistory.builder()
          .waitingId(savedWaiting.getId())
          .createdAt(LocalDateTime.now()) // 동일한 생성 시간
          .waitingStatusId(1L) // "대기중" 상태 ID (1)
          .build();

      waitingHistoryRepository.save(newHistory);

      return savedWaiting;
  }
  
  @Transactional
  public String updateWaitingStatus(Long waitingId, Integer newStatusId) {
      // 1. 상태 값 유효성 검사
      if (newStatusId < 2 || newStatusId > 3) {
          throw new IllegalArgumentException("Invalid status ID. Must be 2 or 3.");
      }

      try {
          // 2. Function 호출 및 결과 반환
          return waitingRepository.callUpdateWaitingStatus(waitingId, newStatusId);
      } catch (Exception e) {
          throw new RuntimeException("Error occurred while updating waiting status.", e);
      }
  }
  
  /**
   * 프로시저 호출 및 결과 반환
   */
  public List<Map<String, Object>> getDynamicRankTable(int restaurantId) {
      // 프로시저 호출 후 임시 테이블에서 데이터 반환
      return waitingRepository.getDynamicRankTable(restaurantId);
  }


  
}
