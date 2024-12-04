package com.example.catchtable.controller;

import com.example.catchtable.domain.Pickup;
import com.example.catchtable.domain.PickupCreateRequestDTO;
import com.example.catchtable.domain.PickupHistory;
import com.example.catchtable.domain.PickupStatusUpdateDTO;
import com.example.catchtable.repository.PickupRepository;
import com.example.catchtable.repository.UtilRepository;
import com.example.catchtable.service.PickupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/pickup")
public class PickupController {

    private final PickupService pickupService;
    private final PickupRepository pickupRepository;
    private final UtilRepository utilRepository;

    @Autowired
    public PickupController(PickupService pickupService, PickupRepository pickupRepository, UtilRepository utilRepository) {
        this.pickupService = pickupService;
        this.pickupRepository = pickupRepository;
        this.utilRepository = utilRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Map<String, Object>>> getPickupDetail(@PathVariable Long id) {
        try {
            Optional<Map<String, Object>> pickupInfo = pickupRepository.getPickupDetail(id);
            if (pickupInfo.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(pickupInfo);
        } catch (Exception e) {
            e.printStackTrace();
            // 에러 응답을 위한 Map 생성
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "An error occurred while fetching pickup info");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Optional.of(errorResponse));
        }
    }

    // get all pickups by userId
    @GetMapping("/by-user/{id}")
    public ResponseEntity<List<Map<String, Object>>> getAllPickupsByUserId(@PathVariable Long id) {
        try {
            List<Map<String, Object>> pickups = pickupRepository.findPickupsByUserId(id);
            if (pickups.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(pickups);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    // update pickup status
    @PostMapping("/status/{id}")
    public ResponseEntity<Optional<PickupHistory>> updatePickupStatus(@PathVariable Long id, @RequestBody PickupStatusUpdateDTO requestBody) {
        String newStatus = requestBody.getStatus();
        Optional<PickupHistory> result = pickupService.updatePickupStatus(id, newStatus);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/by-restaurant/{id}")
    public ResponseEntity<List<Map<String, Object>>> getPickupsByRestaurantId(
            @PathVariable Long id,
            @RequestParam(required = false) LocalDate date) {
        try {
            Optional<LocalDate> optionalPickupDate = Optional.ofNullable(date);
            List<Map<String, Object>> pickups = pickupRepository.findPickupsByRestaurantId(id, optionalPickupDate);
            if (pickups.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(pickups);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    @PostMapping("")
    public ResponseEntity<?> createPickup(@RequestBody PickupCreateRequestDTO requestDTO) {
        try {
            Pickup createdPickup = (Pickup) pickupService.createPickup(requestDTO);
            return ResponseEntity.ok(createdPickup);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    @PostMapping("/cancel/{id}")
    public ResponseEntity<Void> deletePickup(@PathVariable Long id) {
        try {
            int result = utilRepository.softDeleteById("pickup", id);
            if (result == 0) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
