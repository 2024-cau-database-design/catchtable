package com.example.catchtable.service;

import com.example.catchtable.domain.Pickup;
import com.example.catchtable.domain.PickupCreateRequestDTO;
import com.example.catchtable.domain.PickupHistory;
import com.example.catchtable.repository.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PickupService {

  private final PickupRepository pickupRepository;
  private final OrderRepository orderRepository;
  private final PaymentRepository paymentRepository;
  private final PickupHistoryRepository pickupHistoryRepository;
  private final PickupStatusRepository pickupStatusRepository;


  @Autowired
  public PickupService(
          PickupRepository pickupRepository,
          PickupHistoryRepository pickupHistoryRepository,
          PickupStatusRepository pickupStatusRepository,
          OrderRepository orderRepository,
          PaymentRepository paymentRepository
  ) {
    this.pickupRepository = pickupRepository;
    this.pickupHistoryRepository = pickupHistoryRepository;
    this.pickupStatusRepository = pickupStatusRepository;
    this.orderRepository = orderRepository;
    this.paymentRepository = paymentRepository;
  }

  @Transactional
  public Pickup createPickup(PickupCreateRequestDTO pickupRequest) {
    System.out.println("Starting createPickup process...");
    System.out.println("Pickup Request: " + pickupRequest);

    // 1. Validate the request
    validatePickupRequest(pickupRequest);
    System.out.println("Validation completed.");

    // 2. Execute the first procedure: create_booking_and_pickup
    System.out.println("Calling createBookingAnd Pickup procedure...");
    Pickup createdPickup = pickupRepository.createBookingAndPickup(
            pickupRequest.getPickupAt(),
            pickupRequest.getRestaurantId(),
            pickupRequest.getUserId(),
            pickupRequest.getPickupTimeId()
    );
    System.out.println("createBookingAndPickup Result: " + createdPickup);

    Long pickupId = (Long) createdPickup.getId();

    // 3. Convert PickupMenu to JSON format for the second procedure
    String menuJson = pickupRequest.getPickupMenus().stream()
            .map(menu -> String.format("{\"menu_id\":%d,\"quantity\":%d}", menu.getMenuId(), menu.getQuantity()))
            .collect(Collectors.joining(",", "[", "]"));
    System.out.println("Generated menu JSON: " + menuJson);

    // 4. Execute the second procedure: create_order_and_items
    System.out.println("Calling createOrderAndItems procedure...");
    Map<String, Object> orderResult = orderRepository.createOrderAndItems(
            pickupId,
            pickupRequest.getRestaurantId(),
            pickupRequest.getUserId(),
            0, // Example reservation fee
            menuJson
    );
    System.out.println("createOrderAndItems Result: " + orderResult);

    Long orderId = (Long) orderResult.get("order_id");

    // 5. Call createPaymentAndHistory
    System.out.println("Calling createPaymentAndHistory procedure...");
    Map<String, Object> paymentResult = paymentRepository.createPaymentAndHistory(
            orderId,
            (Integer) orderResult.get("total_price"), // Use the total price from order result
            pickupRequest.getPaymentMethod(), // Assume this is added to PickupCreateRequestDTO
            LocalDateTime.now() // Current transaction date
    );
    System.out.println("createPaymentAndHistory Result: " + paymentResult);

    return createdPickup;
  }

  private void validatePickupRequest(PickupCreateRequestDTO pickupRequest) {
    // 유효성 검사
    LocalDateTime now = LocalDateTime.now();

    // 1시간 전에만 예약 가능하도록 확인
    if (pickupRequest.getPickupAt().plusHours(1).isBefore(now)) {
      throw new IllegalArgumentException("Pickup time must be at least 1 hour from now.");
    }
    System.out.println(now);

    // 동일 시간대에 이미 예약된 Pickup이 있는지 확인
    boolean exists = pickupRepository.existsByIdAndDate(pickupRequest.getRestaurantId(), pickupRequest.getPickupAt());
    System.out.println(exists);
    if (exists) {
      throw new IllegalArgumentException("Pickup time is already reserved.");
    }
  }

  public Optional<PickupHistory> updatePickupStatus(Long pickupId, String status) {
    Optional<PickupHistory> pickupHistory = pickupHistoryRepository.findLatestByPickupId(pickupId);
    Long pickupStatusId = pickupStatusRepository.findByType(status)
            .orElseThrow(() -> new IllegalArgumentException("Invalid pickup status: " + status))
            .getId();

    if (pickupHistory.isPresent()) {
      String previousType = pickupStatusRepository.findById(pickupHistory.get().getStatusId()).get().getType();
      if ("AFTER_PICKUP".equals(previousType)) {
        throw new IllegalArgumentException("Pickup has already been completed.");
      }

      Timestamp pickedAt = "AFTER_PICKUP".equals(status) ? Timestamp.valueOf(LocalDateTime.now()) : null;
      PickupHistory newPickupHistory = PickupHistory.fromEntity(
              pickupHistory.get().getId(),
              pickupStatusId,
              pickedAt,
              pickupHistory.get().getPickupTimeId(),
              pickupHistory.get().getPickupAt(),
              pickupHistory.get().getPickupId(),
              LocalDateTime.now()
      );
      pickupHistoryRepository.insert(newPickupHistory);

    }
      return pickupHistoryRepository.findLatestByPickupId(pickupId);
  }
}