package com.learnify.review.service.impl;

import com.learnify.review.service.ReviewService;
import org.springframework.stereotype.Service;
import com.learnify.review.dto.request.ReviewRequest;
import com.learnify.review.dto.response.ReviewResponse;
import com.learnify.review.entity.Review;
import com.learnify.review.exception.ReviewException;
import com.learnify.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private static final int MIN_RATING = 1;
    private static final int MAX_RATING = 5;

    private final ReviewRepository reviewRepository;

    @Override
    @Transactional
    public ReviewResponse createReview(UUID studentId, ReviewRequest request) {
        if (reviewRepository.existsByStudentIdAndCourseId(studentId, request.getCourseId())) {
            throw new ReviewException("You have already reviewed this course");
        }
        Review review = Review.builder()
                .studentId(studentId)
                .courseId(request.getCourseId())
                .rating(request.getRating())
                .comment(request.getComment())
                .visible(true)
                .build();

        review = reviewRepository.save(review);
        log.info("Review created by student {} for course {}", studentId, request.getCourseId());
        return mapToResponse(review);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReviewResponse> getCourseReviews(UUID courseId, Pageable pageable) {
        return reviewRepository.findByCourseIdAndVisible(courseId, true, pageable)
                .map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    @Override
    public Double getCourseAverageRating(UUID courseId) {
        return reviewRepository.getAverageRatingByCourseId(courseId);
    }

    @Override
    @Transactional
    public ReviewResponse updateReview(UUID studentId, UUID reviewId, ReviewRequest request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewException("Review not found"));

        if (!review.getStudentId().equals(studentId)) {
            throw new SecurityException("Not authorized to update this review");
        }

        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setUpdatedAt(LocalDateTime.now());
        return mapToResponse(reviewRepository.save(review));
    }

    private ReviewResponse mapToResponse(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .studentId(review.getStudentId())
                .courseId(review.getCourseId())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .build();
    }

}
