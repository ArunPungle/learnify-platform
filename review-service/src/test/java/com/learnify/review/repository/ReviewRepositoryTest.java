package com.learnify.review.repository;

import com.learnify.review.entity.Review;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private TestEntityManager entityManager;

    private UUID studentId;
    private UUID courseId;

    @BeforeEach
    void setUp() {
        studentId = UUID.randomUUID();
        courseId = UUID.randomUUID();
    }

    // Helper
    private Review saveReview(int rating, boolean visible) {
        Review review = Review.builder()
                .studentId(studentId)
                .courseId(courseId)
                .rating(rating)
                .comment("Test")
                .visible(visible)
                .createdAt(LocalDateTime.now())
                .build();

        return entityManager.persistAndFlush(review);
    }

    // EXISTS BY STUDENT + COURSE
    @Test
    void shouldReturnTrueIfReviewExists() {
        saveReview(5, true);
        boolean exists = reviewRepository.existsByStudentIdAndCourseId(studentId, courseId);
        assertThat(exists).isTrue();
    }

    @Test
    void shouldReturnFalseIfReviewNotExists() {
        boolean exists = reviewRepository.existsByStudentIdAndCourseId(studentId, courseId);
        assertThat(exists).isFalse();
    }

    // FIND BY STUDENT + COURSE
    @Test
    void shouldFindByStudentAndCourse() {
        saveReview(4, true);
        Optional<Review> result = reviewRepository.findByStudentIdAndCourseId(studentId, courseId);
        assertThat(result).isPresent();
        assertThat(result.get().getRating()).isEqualTo(4);
    }

    // FIND BY COURSE + VISIBILITY
    @Test
    void shouldReturnOnlyVisibleReviews() {
        saveReview(5, true);
        saveReview(3, false); // invisible

        Page<Review> result = reviewRepository
                .findByCourseIdAndVisible(courseId, true, PageRequest.of(0, 10));

        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    // AVERAGE RATING
    @Test
    void shouldCalculateAverageRating() {
        saveReview(5, true);
        saveReview(3, true);
        Double avg = reviewRepository.getAverageRatingByCourseId(courseId);
        assertThat(avg).isEqualTo(4.0);
    }

    @Test
    void shouldReturnNullWhenNoReviewsForAverage() {
        Double avg = reviewRepository.getAverageRatingByCourseId(courseId);
        assertThat(avg).isNull();
    }

    // COUNT
    @Test
    void shouldCountVisibleReviews() {
        saveReview(5, true);
        saveReview(3, true);
        saveReview(1, false);
        long count = reviewRepository.countByCourseId(courseId);
        assertThat(count).isEqualTo(2);
    }

    @Test
    void shouldReturnZeroWhenNoReviews() {
        long count = reviewRepository.countByCourseId(courseId);
        assertThat(count).isZero();
    }
}