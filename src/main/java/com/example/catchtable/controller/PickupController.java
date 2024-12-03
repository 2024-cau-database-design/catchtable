package com.example.catchtable.controller;

import com.example.catchtable.domain.Pickup;
import com.example.catchtable.domain.PickupCreateRequestDTO;
import com.example.catchtable.repository.PickupRepository;
import com.example.catchtable.service.PickupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
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

    // get all pickups by restaurantId
    @GetMapping("/restaurant/{id}")
    public ResponseEntity<List<Pickup>> getAllPickupsByRestaurantId(@PathVariable Long id) {
        try {
            List<Pickup> pickups = pickupRepository.findPickupsByRestaurantId(id, Optional.empty());
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

    @GetMapping("/restaurant/{id}/today")
    public ResponseEntity<List<Pickup>> getTodayPickupsByRestaurantId(@PathVariable Long id) {
        try {
            List<Pickup> pickups = pickupRepository.findPickupsByRestaurantId(id, Optional.of(LocalDate.now()));
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
}
