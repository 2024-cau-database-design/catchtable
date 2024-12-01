package com.example.catchtable.controller;

import com.example.catchtable.domain.Pickup;
import com.example.catchtable.domain.PickupCreateRequestDTO;
import com.example.catchtable.repository.PickupRepository;
import com.example.catchtable.service.PickupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/pickup")
public class PickupController {

    private final PickupService pickupService;
    private final PickupRepository pickupRepository;

    @Autowired
    public PickupController(PickupService pickupService, PickupRepository pickupRepository) {
        this.pickupService = pickupService;
        this.pickupRepository = pickupRepository;
    }

    @GetMapping("/{id}")
    public Optional<Pickup> getById(@PathVariable Long id){
        return pickupRepository.findById(id);
    }

    @PostMapping("")
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
            e.printStackTrace();
            return ResponseEntity.status(500).body("An unexpected error occurred.");
        }
    }
}