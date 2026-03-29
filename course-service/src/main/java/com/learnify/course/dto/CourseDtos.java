package com.learnify.course.dto;

import com.learnify.course.entity.CourseStatus;
import com.learnify.course.entity.DifficultyLevel;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class CourseDtos {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateCourseRequest {
        @NotBlank(message = "Course title is required")
        @Size(min = 10, max = 200, message = "Title must be between 10 and 200 characters")
        private String title;

        @NotBlank(message = "Description is required")
        @Size(min = 50, message = "Description must be at least 50 characters")
        private String description;

        @Size(max = 500)
        private String shortDescription;

        private UUID categoryId;

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.0", message = "Price cannot be negative")
        private BigDecimal price;

        private BigDecimal discountPrice;

        private DifficultyLevel difficultyLevel;

        private String language;

        private String thumbnailUrl;

        private List<String> requirements;

        private List<String> objectives;

        @Builder.Default
        private boolean free = false;

        @Builder.Default
        private boolean certificateAvailable = false;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateCourseRequest {
        @Size(min = 10, max = 200)
        private String title;

        private String description;

        @Size(max = 500)
        private String shortDescription;

        private UUID categoryId;

        @DecimalMin(value = "0.0")
        private BigDecimal price;

        private BigDecimal discountPrice;

        private DifficultyLevel difficultyLevel;

        private String language;

        private String thumbnailUrl;

        private String previewVideoUrl;

        private List<String> requirements;

        private List<String> objectives;

        private Boolean certificateAvailable;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CourseResponse {
        private UUID id;
        private String title;
        private String description;
        private String shortDescription;
        private UUID instructorId;
        private String instructorName;
        private CategorySummary category;
        private BigDecimal price;
        private BigDecimal discountPrice;
        private CourseStatus status;
        private DifficultyLevel difficultyLevel;
        private String language;
        private String thumbnailUrl;
        private String previewVideoUrl;
        private Integer totalDurationMinutes;
        private Integer totalLessons;
        private Integer totalEnrollments;
        private BigDecimal averageRating;
        private Integer totalReviews;
        private List<String> requirements;
        private List<String> objectives;
        private boolean free;
        private boolean certificateAvailable;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private LocalDateTime publishedAt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CourseSummary {
        private UUID id;
        private String title;
        private String shortDescription;
        private String instructorName;
        private BigDecimal price;
        private BigDecimal discountPrice;
        private BigDecimal averageRating;
        private Integer totalEnrollments;
        private Integer totalLessons;
        private String thumbnailUrl;
        private DifficultyLevel difficultyLevel;
        private String language;
        private boolean free;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategorySummary {
        private UUID id;
        private String name;
        private String slug;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CourseSearchRequest {
        private String searchTerm;
        private UUID categoryId;
        private DifficultyLevel difficultyLevel;
        private BigDecimal minPrice;
        private BigDecimal maxPrice;
        private String language;
        private String sortBy; // "price", "rating", "enrollments", "newest"
        private int page = 0;
        private int size = 20;
    }
}