package com.example.catchtable.service;

import com.example.catchtable.domain.PickupCreateRequestDTO;
import com.example.catchtable.domain.Pickup;
import com.example.catchtable.domain.Booking;
import com.example.catchtable.domain.Order;
import com.example.catchtable.domain.OrderItem;
import com.example.catchtable.domain.Payment;
import com.example.catchtable.domain.PaymentHistory;
import com.example.catchtable.repository.PickupRepository;
import com.example.catchtable.repository.BookingRepository;
//import com.example.catchtable.repository.PickupHistoryRepository;
import com.example.catchtable.repository.OrderRepository;
import com.example.catchtable.repository.OrderItemRepository;
import com.example.catchtable.repository.PaymentRepository;
import com.example.catchtable.repository.PaymentHistoryRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PickupService {

  private final PickupRepository pickupRepository;
  private final BookingRepository bookingRepository;
//  private final PickupHistoryRepository pickupHistoryRepository;
  private final OrderRepository orderRepository;
  private final OrderItemRepository orderItemRepository;
  private final PaymentRepository paymentRepository;
  private final PaymentHistoryRepository paymentHistoryRepository;

  @Autowired
  public PickupService(
          PickupRepository pickupRepository,
          BookingRepository bookingRepository,
//          PickupHistoryRepository pickupHistoryRepository,
          OrderRepository orderRepository,
          OrderItemRepository orderItemRepository,
          PaymentRepository paymentRepository,
          PaymentHistoryRepository paymentHistoryRepository
  ) {
    this.pickupRepository = pickupRepository;
    this.bookingRepository = bookingRepository;
//    this.pickupHistoryRepository = pickupHistoryRepository;
    this.orderRepository = orderRepository;
    this.orderItemRepository = orderItemRepository;
    this.paymentRepository = paymentRepository;
    this.paymentHistoryRepository = paymentHistoryRepository;
  }

  @Transactional
  public void createPickup(PickupCreateRequestDTO pickupRequest) {
    // 1. 유효성 검사
    validatePickupRequest(pickupRequest);

    Map<String, Object> result = pickupRepository.createBookingAndPickup(pickupRequest.getPickupAt(), pickupRequest.getRestaurantId(), pickupRequest.getUserId(), 1L);

    // 2. Booking 생성
//    Booking booking = Booking.fromEntity(
//            null, // id
//            "pickup"
//    );
//    Long createdBookingId = bookingRepository.insert(booking);
//
//    // 3. Pickup 생성
//    Pickup pickup = Pickup.fromEntity(
//            createdBookingId, // id는 새로 생성되므로 null
//            null, // pickedAt은 아직 처리되지 않음
//            pickupRequest.getPickupAt(),
//            1L, // test pickupTimeId
//            pickupRequest.getRestaurantId(),
//            Timestamp.valueOf(LocalDateTime.now()), // createdAt
//            Timestamp.valueOf(LocalDateTime.now()), // updatedAt
//            false, // isDeleted
//            null   // deletedAt
//    );
//    ObjectMapper mapper = new ObjectMapper();
//    mapper.registerModule(new JavaTimeModule()); // JavaTimeModule 등록
//
//    pickupRepository.insert(pickup);

//    // 4. PickupHistory 생성
//    PickupHistory pickupHistory = PickupHistory.fromEntity(
//            null, // id
//            pickup.getId(), // pickupId
//            "CREATED", // status
//            Timestamp.valueOf(LocalDateTime.now()) // createdAt
//    );
//    pickupHistoryRepository.save(pickupHistory);
//
//    // 5. Order 생성
//    Order order = Order.fromEntity(
//            null, // id
//            pickup.getId(), // pickupId
//            Timestamp.valueOf(LocalDateTime.now()) // createdAt
//    );
//    orderRepository.save(order);
//
//    // 6. OrderItem 생성
//    OrderItem orderItem = OrderItem.fromEntity(
//            null, // id
//            order.getId(), // orderId
//            "Default Item", // itemName
//            Timestamp.valueOf(LocalDateTime.now()) // createdAt
//    );
//    orderItemRepository.save(orderItem);
//
//    // 7. Payment 생성
//    Payment payment = Payment.fromEntity(
//            null, // id
//            order.getId(), // orderId
//            pickupRequest.getPaymentAmount(), // amount
//            Timestamp.valueOf(LocalDateTime.now()) // createdAt
//    );
//    paymentRepository.save(payment);
//
//    // 8. PaymentHistory 생성
//    PaymentHistory paymentHistory = PaymentHistory.fromEntity(
//            null, // id
//            payment.getId(), // paymentId
//            "INITIATED", // status
//            Timestamp.valueOf(LocalDateTime.now()) // createdAt
//    );
//    paymentHistoryRepository.save(paymentHistory);
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
}