package com.learnify.review.controller;

import com.learnify.review.dto.request.ReviewRequest;
import com.learnify.review.dto.response.ReviewResponse;
import com.learnify.review.service.ReviewService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Tag(name = "Reviews", description = "Course review management")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(@Valid @RequestBody ReviewRequest request, @RequestHeader("X-User-Id") UUID studentId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reviewService.createReview(studentId, request));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<Page<ReviewResponse>> getCourseReviews(@PathVariable UUID courseId, Pageable pageable) {
        return ResponseEntity.ok(reviewService.getCourseReviews(courseId, pageable));
    }

    @GetMapping("/course/{courseId}/rating")
    public ResponseEntity<Map<String, Double>> getCourseRating(@PathVariable UUID courseId) {
        return ResponseEntity.ok(Map.of("averageRating", reviewService.getCourseAverageRating(courseId)));
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponse> updateReview(@PathVariable UUID reviewId, @Valid @RequestBody ReviewRequest request, @RequestHeader("X-User-Id") UUID studentId) {
        return ResponseEntity.ok(reviewService.updateReview(studentId, reviewId, request));
    }
}
