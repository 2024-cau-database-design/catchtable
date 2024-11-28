package com.example.catchtable.domain;

import static com.example.catchtable.util.LocalDateTimeUtil.toLocalDateTimeOrNull;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.sql.Timestamp;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewImage {

  private Integer id;
  private Integer reviewId;
  // private String name;
  private String url;
  private LocalDateTime createdAt;
  private Boolean isDeleted;
  private LocalDateTime deletedAt;

  // 정적 팩토리 메소드
  public static ReviewImage fromEntity(
      final Integer id, final Integer reviewId, final String url,
      final Timestamp createdAt, final Boolean isDeleted, final Timestamp deletedAt) {
    ReviewImage reviewImage = new ReviewImage();
    reviewImage.id = id;
    reviewImage.reviewId = reviewId;
    // reviewImage.name = name;
    reviewImage.url = url;
    reviewImage.createdAt = createdAt.toLocalDateTime();
    reviewImage.isDeleted = isDeleted;
    reviewImage.deletedAt = toLocalDateTimeOrNull(deletedAt);
    return reviewImage;
  }
}