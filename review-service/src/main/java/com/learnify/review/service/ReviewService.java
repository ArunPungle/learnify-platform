package com.learnify.review.service;

import com.learnify.review.dto.request.ReviewRequest;
import com.learnify.review.dto.response.ReviewResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ReviewService {

    ReviewResponse createReview(UUID studentId, @Valid ReviewRequest request);

    Page<ReviewResponse> getCourseReviews(UUID courseId, Pageable pageable);

    Double getCourseAverageRating(UUID courseId);

    ReviewResponse updateReview(UUID studentId, UUID reviewId, @Valid ReviewRequest request);

}
