package com.learnify.course.dto.request;

import com.learnify.course.entity.CourseLevel;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class CreateCourseRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 200)
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @Size(max = 500)
    private String shortDescription;

    private UUID categoryId;

    @NotNull
    @DecimalMin("0.00")
    private BigDecimal price;

    private BigDecimal discountPrice;
    private String thumbnailUrl;
    private String previewVideoUrl;
    private CourseLevel level;
    private List<String> requirements;
    private List<String> objectives;
}
