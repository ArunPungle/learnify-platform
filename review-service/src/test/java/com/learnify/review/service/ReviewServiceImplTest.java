package com.learnify.review.service;

import com.learnify.review.dto.request.ReviewRequest;
import com.learnify.review.dto.response.ReviewResponse;
import com.learnify.review.entity.Review;
import com.learnify.review.exception.ReviewException;
import com.learnify.review.repository.ReviewRepository;
import com.learnify.review.service.impl.ReviewServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.assertj.core.api.Assertions.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class ReviewServiceImplTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    private UUID studentId;
    private UUID courseId;
    private UUID reviewId;

    @BeforeEach
    void setUp() {
        studentId = UUID.randomUUID();
        courseId = UUID.randomUUID();
        reviewId = UUID.randomUUID();
    }

    // CREATE REVIEW
    @Test
    void shouldCreateReviewSuccessfully() {
        ReviewRequest request = buildRequest(5, " Great course ");
        when(reviewRepository.existsByStudentIdAndCourseId(studentId, courseId))
                .thenReturn(false);

        Review savedReview = Review.builder()
                .id(UUID.randomUUID())
                .studentId(studentId)
                .courseId(courseId)
                .rating(5)
                .comment("Great course")
                .createdAt(LocalDateTime.now())
                .build();

        when(reviewRepository.save(any())).thenReturn(savedReview);
        ReviewResponse response = reviewService.createReview(studentId, request);
        assertThat(response).isNotNull();
        assertThat(response.getRating()).isEqualTo(5);
        assertThat(response.getComment()).isEqualTo("Great course");

        verify(reviewRepository).save(any());
        verifyNoMoreInteractions(reviewRepository);
    }

    @Test
    void shouldThrowIfDuplicateReview() {
        ReviewRequest request = buildRequest(5, "Test");

        when(reviewRepository.existsByStudentIdAndCourseId(studentId, courseId))
                .thenReturn(true);

        assertThatThrownBy(() -> reviewService.createReview(studentId, request))
                .isInstanceOf(ReviewException.class);
    }

    @Test
    void shouldThrowIfRatingInvalid_low() {
        ReviewRequest request = buildRequest(0, "Bad");

        assertThatThrownBy(() -> reviewService.createReview(studentId, request))
                .isInstanceOf(ReviewException.class);
    }

    @Test
    void shouldThrowIfRatingInvalid_high() {
        ReviewRequest request = buildRequest(6, "Bad");

        assertThatThrownBy(() -> reviewService.createReview(studentId, request))
                .isInstanceOf(ReviewException.class);
    }

    @Test
    void shouldThrowIfRequestNull() {
        assertThatThrownBy(() -> reviewService.createReview(studentId, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldThrowIfStudentIdNull() {
        ReviewRequest request = buildRequest(5, "Test");
        assertThatThrownBy(() -> reviewService.createReview(null, request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldHandleDataIntegrityViolation() {
        ReviewRequest request = buildRequest(5, "Test");
        when(reviewRepository.existsByStudentIdAndCourseId(studentId, courseId))
                .thenReturn(false);

        when(reviewRepository.save(any()))
                .thenThrow(DataIntegrityViolationException.class);

        assertThatThrownBy(() -> reviewService.createReview(studentId, request))
                .isInstanceOf(ReviewException.class);
    }

    @Test
    void shouldTrimComment() {
        ReviewRequest request = buildRequest(5, "  spaced  ");

        when(reviewRepository.existsByStudentIdAndCourseId(studentId, courseId))
                .thenReturn(false);

        when(reviewRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ReviewResponse response = reviewService.createReview(studentId, request);

        assertThat(response.getComment()).isEqualTo("spaced");
    }

    // GET REVIEWS

    @Test
    void shouldReturnPagedReviews() {
        Pageable pageable = PageRequest.of(0, 10);

        Review review = Review.builder()
                .id(reviewId)
                .studentId(studentId)
                .courseId(courseId)
                .rating(4)
                .comment("Good")
                .createdAt(LocalDateTime.now())
                .build();

        when(reviewRepository.findByCourseIdAndVisible(courseId, true, pageable))
                .thenReturn(new PageImpl<>(List.of(review)));

        Page<ReviewResponse> result =
                reviewService.getCourseReviews(courseId, pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    void shouldReturnEmptyPage() {
        Pageable pageable = PageRequest.of(0, 10);

        when(reviewRepository.findByCourseIdAndVisible(courseId, true, pageable))
                .thenReturn(Page.empty());

        Page<ReviewResponse> result =
                reviewService.getCourseReviews(courseId, pageable);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldThrowIfCourseIdNull_getReviews() {
        Pageable pageable = PageRequest.of(0, 10);

        assertThatThrownBy(() ->
                reviewService.getCourseReviews(null, pageable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldThrowIfPageableNull() {
        assertThatThrownBy(() ->
                reviewService.getCourseReviews(courseId, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // AVERAGE RATING
    @Test
    void shouldReturnAverageRating() {
        when(reviewRepository.getAverageRatingByCourseId(courseId))
                .thenReturn(4.5);

        Double result = reviewService.getCourseAverageRating(courseId);
        assertThat(result).isEqualTo(4.5);
    }

    @Test
    void shouldReturnZeroIfNullAverage() {
        when(reviewRepository.getAverageRatingByCourseId(courseId))
                .thenReturn(null);

        Double result = reviewService.getCourseAverageRating(courseId);

        assertThat(result).isEqualTo(0.0);
    }

    @Test
    void shouldThrowIfCourseIdNull_average() {
        assertThatThrownBy(() ->
                reviewService.getCourseAverageRating(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // UPDATE REVIEW

    @Test
    void shouldUpdateReviewSuccessfully() {
        Review review = buildReview(studentId, 3, "Old");
        ReviewRequest request = buildRequest(5, "Updated");
        when(reviewRepository.findById(reviewId))
                .thenReturn(Optional.of(review));

        when(reviewRepository.save(any()))
                .thenReturn(review);

        ReviewResponse response = reviewService.updateReview(studentId, reviewId, request);
        assertThat(response.getRating()).isEqualTo(5);
        assertThat(response.getComment()).isEqualTo("Updated");
    }

    @Test
    void shouldPartiallyUpdateOnlyComment() {
        Review review = buildReview(studentId, 3, "Old");
        ReviewRequest request = buildRequest(null, "Only Comment");
        when(reviewRepository.findById(reviewId))
                .thenReturn(Optional.of(review));

        when(reviewRepository.save(any()))
                .thenReturn(review);

        ReviewResponse response = reviewService.updateReview(studentId, reviewId, request);
        assertThat(response.getRating()).isEqualTo(3);
        assertThat(response.getComment()).isEqualTo("Only Comment");
    }

    @Test
    void shouldThrowIfReviewNotFound() {
        when(reviewRepository.findById(reviewId))
                .thenReturn(Optional.empty());

        ReviewRequest request = buildRequest(5, "Test");
        assertThatThrownBy(() ->
                reviewService.updateReview(studentId, reviewId, request))
                .isInstanceOf(ReviewException.class);
    }

    @Test
    void shouldThrowIfUnauthorized() {
        Review review = buildReview(UUID.randomUUID(), 3, "Old");
        when(reviewRepository.findById(reviewId))
                .thenReturn(Optional.of(review));

        ReviewRequest request = buildRequest(5, "Test");
        assertThatThrownBy(() ->
                reviewService.updateReview(studentId, reviewId, request))
                .isInstanceOf(SecurityException.class);
    }

    @Test
    void shouldThrowIfInvalidRating_update() {
        Review review = buildReview(studentId, 3, "Old");
        when(reviewRepository.findById(reviewId))
                .thenReturn(Optional.of(review));

        ReviewRequest request = buildRequest(10, "Bad");
        assertThatThrownBy(() ->
                reviewService.updateReview(studentId, reviewId, request))
                .isInstanceOf(ReviewException.class);
    }

    // HELPERS
    private ReviewRequest buildRequest(Integer rating, String comment) {
        return ReviewRequest.builder()
                .courseId(courseId)
                .rating(rating)
                .comment(comment)
                .build();
    }

    private Review buildReview(UUID studentId, int rating, String comment) {
        return Review.builder()
                .id(reviewId)
                .studentId(studentId)
                .courseId(courseId)
                .rating(rating)
                .comment(comment)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
