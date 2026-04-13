package com.learnify.review.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponse {

    private UUID id;
    private UUID studentId;
    private UUID courseId;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
}
