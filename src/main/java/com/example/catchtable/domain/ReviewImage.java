package com.example.catchtable.domain;

import static com.example.catchtable.util.LocalDateTimeUtil.toLocalDateTimeOrNull;

import java.sql.Timestamp;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewImage {

  private Long id; // int unsigned -> Long
  private Long reviewId; // int unsigned -> Long,  review_id -> reviewId
  private String name;
  private String url;

  // 정적 팩토리 메소드
  public static ReviewImage fromEntity(
      final Long id, final Long reviewId, final String name, final String url) {
    ReviewImage reviewImage = new ReviewImage();
    reviewImage.id = id;
    reviewImage.reviewId = reviewId;
    reviewImage.name = name;
    reviewImage.url = url;
    return reviewImage;
  }
}