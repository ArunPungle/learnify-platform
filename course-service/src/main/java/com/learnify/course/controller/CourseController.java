package com.learnify.course.controller;

import com.learnify.course.dto.request.CreateCourseRequest;
import com.learnify.course.dto.response.CourseResponse;
import com.learnify.course.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
@Tag(name = "Courses", description = "Course management APIs")
@SecurityRequirement(name = "Bearer Auth")
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    @Operation(summary = "Create a new course")
    public ResponseEntity<CourseResponse> createCourse(@Valid @RequestBody CreateCourseRequest request, @RequestHeader("X-User-Name") String instructorEmail, @RequestHeader("X-User-Id") UUID instructorId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(courseService.createCourse(request, instructorId, instructorEmail));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get course by ID")
    public ResponseEntity<CourseResponse> getCourse(@PathVariable UUID id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @GetMapping
    @Operation(summary = "Search and filter courses")
    public ResponseEntity<Page<CourseResponse>> searchCourses(@RequestParam(required = false) UUID categoryId, @RequestParam(required = false) String search, @PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {
        return ResponseEntity.ok(courseService.searchCourses(categoryId, search, pageable));
    }

    @GetMapping("/instructor/{instructorId}")
    @Operation(summary = "Get courses by instructor")
    public ResponseEntity<Page<CourseResponse>> getInstructorCourses(@PathVariable UUID instructorId, @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(courseService.getInstructorCourses(instructorId, pageable));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a course")
    public ResponseEntity<CourseResponse> updateCourse(@PathVariable UUID id, @Valid @RequestBody CreateCourseRequest request, @RequestHeader("X-User-Id") UUID instructorId) {
        return ResponseEntity.ok(courseService.updateCourse(id, request, instructorId));
    }

    @PatchMapping("/{id}/publish")
    @Operation(summary = "Publish a course")
    public ResponseEntity<CourseResponse> publishCourse(@PathVariable UUID id, @RequestHeader("X-User-Id") UUID instructorId) {
        return ResponseEntity.ok(courseService.publishCourse(id, instructorId));
    }

    @GetMapping("/popular")
    @Operation(summary = "Get popular courses")
    public ResponseEntity<List<CourseResponse>> getPopularCourses(@RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(courseService.getPopularCourses(limit));
    }
}

