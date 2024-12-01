package com.example.catchtable.controller;

import com.example.catchtable.domain.PickupCreateRequestDTO;
import com.example.catchtable.service.PickupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pickup")
public class PickupController {

    private final PickupService pickupService;

    @Autowired
    public PickupController(PickupService pickupService) {
        this.pickupService = pickupService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createPickup(@RequestBody PickupCreateRequestDTO requestDTO) {
        try {
            // DTO를 서비스로 전달하여 처리
            pickupService.createPickup(requestDTO);
            return ResponseEntity.ok("Pickup created successfully.");
        } catch (IllegalArgumentException e) {
            // 유효성 검사 실패 또는 비즈니스 로직 오류
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // 기타 예외 처리
            return ResponseEntity.status(500).body("An unexpected error occurred.");
        }
    }
}