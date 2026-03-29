package com.learnify.course;

import com.learnify.course.dto.request.CreateCourseRequest;
import com.learnify.course.dto.response.CourseResponse;
import com.learnify.course.entity.Course;
import com.learnify.course.entity.CourseStatus;
import com.learnify.course.exception.CourseNotFoundException;
import com.learnify.course.mapper.CourseMapper;
import com.learnify.course.repository.CategoryRepository;
import com.learnify.course.repository.CourseRepository;
import com.learnify.course.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Course Service Tests")
class CourseServiceTest {

    @Mock private CourseRepository courseRepository;
    @Mock private CategoryRepository categoryRepository;
    @Mock private CourseMapper courseMapper;

    @InjectMocks private CourseService courseService;

    private UUID instructorId;
    private CreateCourseRequest request;

    @BeforeEach
    void setUp() {
        instructorId = UUID.randomUUID();
        request = new CreateCourseRequest();
        request.setTitle("Advanced Spring Boot");
        request.setDescription("Learn Spring Boot microservices");
        request.setPrice(BigDecimal.valueOf(79.99));
    }

    @Test
    @DisplayName("Should create course with DRAFT status")
    void shouldCreateCourseAsDraft() {
        Course course = Course.builder()
                .id(UUID.randomUUID())
                .title(request.getTitle())
                .instructorId(instructorId)
                .status(CourseStatus.DRAFT)
                .build();

        CourseResponse response = new CourseResponse();
        response.setId(course.getId());
        response.setTitle(course.getTitle());
        response.setStatus("DRAFT");

        when(courseMapper.toEntity(request)).thenReturn(course);
        when(courseRepository.save(any())).thenReturn(course);
        when(courseMapper.toResponse(course)).thenReturn(response);

        CourseResponse result = courseService.createCourse(request, instructorId, "instructor@example.com");

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo("DRAFT");
        verify(courseRepository).save(any());
    }

    @Test
    @DisplayName("Should throw not found when course does not exist")
    void shouldThrowWhenCourseNotFound() {
        UUID nonExistentId = UUID.randomUUID();
        when(courseRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courseService.getCourseById(nonExistentId))
                .isInstanceOf(CourseNotFoundException.class);
    }
}
