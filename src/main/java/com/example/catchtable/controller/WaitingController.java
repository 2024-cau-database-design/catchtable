package com.example.catchtable.controller;

import com.example.catchtable.domain.Waiting;
import com.example.catchtable.service.WaitingService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/waiting")
@RequiredArgsConstructor
public class WaitingController {

    private final WaitingService waitingService;

    // 웨이팅 등록
    @PostMapping
    public ResponseEntity<Waiting> createWaiting(
        @RequestParam Long customerId,
        @RequestParam Integer guestCount,
        @RequestParam Long restaurantId) {
        // Waiting 등록
        Waiting newWaiting = waitingService.createWaiting(customerId, guestCount, restaurantId);
        return ResponseEntity.status(HttpStatus.CREATED).body(newWaiting);
    	}
	
    // 웨이팅 상태 업데이트
    @PostMapping("/update-status")
    public ResponseEntity<String> updateWaitingStatus(
        @RequestParam Long waitingId,
        @RequestParam Integer newStatusId) {
        try {
            // 상태 업데이트 호출
            String result = waitingService.updateWaitingStatus(waitingId, newStatusId);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            // 잘못된 상태 ID 처리
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            // 기타 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update waiting status.");
        }
    }
    
    /**
    * 임시 테이블 데이터 반환 API
    */
   @GetMapping("/dynamic-rank")
   public ResponseEntity<List<Map<String, Object>>> getDynamicRankTable(@RequestParam int restaurantId) {
       // Service를 호출하여 결과를 반환
       List<Map<String, Object>> result = waitingService.getDynamicRankTable(restaurantId);
       return ResponseEntity.ok(result);
   }
    
}
