package com.learnify.course.service.Impl;

import com.learnify.course.dto.request.CreateCourseRequest;
import com.learnify.course.dto.response.CourseResponse;
import com.learnify.course.entity.Category;
import com.learnify.course.entity.Course;
import com.learnify.course.entity.CourseStatus;
import com.learnify.course.exception.CourseNotFoundException;
import com.learnify.course.mapper.CourseMapper;
import com.learnify.course.repository.CategoryRepository;
import com.learnify.course.repository.CourseRepository;
import com.learnify.course.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    private final CourseMapper courseMapper;

    @Transactional
    @Override
    public CourseResponse createCourse(CreateCourseRequest request, UUID instructorId, String instructorName) {
        Course course = courseMapper.toEntity(request);
        course.setInstructorId(instructorId);
        course.setInstructorName(instructorName);
        course.setStatus(CourseStatus.DRAFT);

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new CourseNotFoundException("Category not found"));
            course.setCategory(category);
        }

        course = courseRepository.save(course);
        log.info("Course created: {} by instructor: {}", course.getId(), instructorId);
        return courseMapper.toResponse(course);
    }

    @Cacheable(value = "courses", key = "#id")
    @Transactional(readOnly = true)
    @Override
    public CourseResponse getCourseById(UUID id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException("Course not found: " + id));
        return courseMapper.toResponse(course);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<CourseResponse> searchCourses(UUID categoryId, String search, Pageable pageable) {
        return courseRepository.searchCourses(categoryId, search, pageable)
                .map(courseMapper::toResponse);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<CourseResponse> getInstructorCourses(UUID instructorId, Pageable pageable) {
        return courseRepository.findByInstructorId(instructorId, pageable)
                .map(courseMapper::toResponse);
    }

    @CacheEvict(value = "courses", key = "#id")
    @Transactional
    @Override
    public CourseResponse updateCourse(UUID id, CreateCourseRequest request, UUID instructorId) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException("Course not found: " + id));

        if (!course.getInstructorId().equals(instructorId)) {
            throw new SecurityException("Not authorized to update this course");
        }

        courseMapper.updateCourseFromRequest(request, course);
        course = courseRepository.save(course);
        return courseMapper.toResponse(course);
    }

    @CacheEvict(value = "courses", key = "#id")
    @Transactional
    @Override
    public CourseResponse publishCourse(UUID id, UUID instructorId) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException("Course not found: " + id));

        if (!course.getInstructorId().equals(instructorId)) {
            throw new SecurityException("Not authorized to publish this course");
        }

        course.setStatus(CourseStatus.PUBLISHED);
        course.setPublishedAt(LocalDateTime.now());
        course = courseRepository.save(course);
        log.info("Course published: {}", id);
        return courseMapper.toResponse(course);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CourseResponse> getPopularCourses(int limit) {
        return courseRepository.findTopPopularCourses(Pageable.ofSize(limit))
                .stream()
                .map(courseMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void updateCourseRating(UUID courseId, double newAverageRating, int totalReviews) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found: " + courseId));
        course.setAverageRating(newAverageRating);
        course.setTotalReviews(totalReviews);
        courseRepository.save(course);
    }

    @Transactional
    @Override
    public void incrementEnrollmentCount(UUID courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found: " + courseId));
        course.setTotalEnrollments(course.getTotalEnrollments() + 1);
        courseRepository.save(course);
    }
}

