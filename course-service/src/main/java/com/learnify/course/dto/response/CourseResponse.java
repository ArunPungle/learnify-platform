package com.learnify.course.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseResponse {

    private UUID id;
    private String title;
    private String description;
    private String shortDescription;
    private UUID instructorId;
    private String instructorName;
    private String categoryName;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private String thumbnailUrl;
    private String status;
    private String level;
    private Double averageRating;
    private Integer totalReviews;
    private Integer totalEnrollments;
    private Double durationHours;
    private List<String> requirements;
    private List<String> objectives;
    private LocalDateTime createdAt;
    private LocalDateTime publishedAt;

}
