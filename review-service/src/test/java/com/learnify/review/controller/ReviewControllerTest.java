package com.learnify.review.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learnify.review.dto.request.ReviewRequest;
import com.learnify.review.dto.response.ReviewResponse;
import com.learnify.review.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReviewController.class)
public class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

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
    void shouldCreateReview() throws Exception {
        ReviewRequest request = ReviewRequest.builder()
                .courseId(courseId)
                .rating(5)
                .comment("Great")
                .build();

        ReviewResponse response = ReviewResponse.builder()
                .id(reviewId)
                .courseId(courseId)
                .studentId(studentId)
                .rating(5)
                .comment("Great")
                .build();

        when(reviewService.createReview(eq(studentId), any()))
                .thenReturn(response);

        mockMvc.perform(post("/api/reviews")
                        .header("X-User-Id", studentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.rating").value(5))
                .andExpect(jsonPath("$.comment").value("Great"));
    }

    @Test
    void shouldFailValidation_whenInvalidRating() throws Exception {
        ReviewRequest request = ReviewRequest.builder()
                .courseId(courseId)
                .rating(10) // invalid
                .comment("Bad")
                .build();

        mockMvc.perform(post("/api/reviews")
                        .header("X-User-Id", studentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(request)))
                .andExpect(status().isBadRequest());
    }

    // GET REVIEWS
    @Test
    void shouldReturnCourseReviews() throws Exception {
        ReviewResponse review = ReviewResponse.builder()
                .id(reviewId)
                .courseId(courseId)
                .rating(4)
                .comment("Good")
                .build();

        Page<ReviewResponse> page = new PageImpl<>(List.of(review));
        when(reviewService.getCourseReviews(eq(courseId), any()))
                .thenReturn(page);

        mockMvc.perform(get("/api/reviews/course/" + courseId)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].rating").value(4));
    }

    @Test
    void shouldHandleEmptyReviews() throws Exception {
        when(reviewService.getCourseReviews(eq(courseId), any()))
                .thenReturn(Page.empty());

        mockMvc.perform(get("/api/reviews/course/" + courseId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty());
    }

    //  AVERAGE RATING
    @Test
    void shouldReturnAverageRating() throws Exception {
        when(reviewService.getCourseAverageRating(courseId))
                .thenReturn(4.5);

        mockMvc.perform(get("/api/reviews/course/" + courseId + "/rating"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.averageRating").value(4.5));
    }

    // UPDATE REVIEW
    @Test
    void shouldUpdateReview() throws Exception {
        ReviewRequest request = ReviewRequest.builder()
                .courseId(courseId)
                .rating(5)
                .comment("Updated")
                .build();

        ReviewResponse response = ReviewResponse.builder()
                .id(reviewId)
                .rating(5)
                .comment("Updated")
                .build();

        when(reviewService.updateReview(eq(studentId), eq(reviewId), any()))
                .thenReturn(response);

        mockMvc.perform(put("/api/reviews/" + reviewId)
                        .header("X-User-Id", studentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comment").value("Updated"));
    }

    // EDGE CASES
    @Test
    void shouldFail_whenMissingHeader() throws Exception {

        ReviewRequest request = ReviewRequest.builder()
                .courseId(courseId)
                .rating(5)
                .comment("Test")
                .build();

        mockMvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldFail_whenBodyIsEmpty() throws Exception {
        mockMvc.perform(post("/api/reviews")
                        .header("X-User-Id", studentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    // UTILS
    private String asJson(Object obj) throws Exception {
        return new ObjectMapper().writeValueAsString(obj);
    }

}
