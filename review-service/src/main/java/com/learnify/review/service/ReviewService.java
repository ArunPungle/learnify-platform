package com.learnify.review.service;

import com.learnify.review.dto.request.ReviewRequest;
import com.learnify.review.dto.response.ReviewResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ReviewService {

    /**
     * Creates a new review for a course by a student.
     *
     * @param studentId the unique identifier of the student creating the review
     * @param request the review request containing rating, comment, and course details
     * @return the created review as a {@link ReviewResponse}
     */
    ReviewResponse createReview(UUID studentId, @Valid ReviewRequest request);

    /**
     * Retrieves a paginated list of reviews for a specific course.
     *
     * @param courseId the unique identifier of the course
     * @param pageable pagination and sorting information
     * @return a page of {@link ReviewResponse} containing course reviews
     */
    Page<ReviewResponse> getCourseReviews(UUID courseId, Pageable pageable);

    /**
     * Calculates the average rating for a specific course.
     *
     * @param courseId the unique identifier of the course
     * @return the average rating of the course, or null if no reviews exist
     */
    Double getCourseAverageRating(UUID courseId);

    /**
     * Updates an existing review created by a student.
     *
     * @param studentId the unique identifier of the student updating the review
     * @param reviewId the unique identifier of the review to be updated
     * @param request the updated review details
     * @return the updated review as a {@link ReviewResponse}
     */
    ReviewResponse updateReview(UUID studentId, UUID reviewId, @Valid ReviewRequest request);

}
