package com.learnify.course.service;

import com.learnify.course.dto.request.CreateCourseRequest;
import com.learnify.course.dto.response.CourseResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface CourseService {

    /**
     * Creates a new course.
     *
     * @param request         the course creation request payload
     * @param instructorId    the ID of the instructor creating the course
     * @param instructorEmail the email of the instructor
     * @return created course response
     */
    CourseResponse createCourse(CreateCourseRequest request, UUID instructorId, String instructorEmail);

    /**
     * Retrieves a course by its ID.
     *
     * @param courseId the course ID
     * @return course response
     */
    CourseResponse getCourseById(UUID courseId);

    /**
     * Searches courses with optional filtering by category and search keyword.
     *
     * @param categoryId optional category ID
     * @param search     optional search keyword
     * @param pageable   pagination information
     * @return paginated list of courses
     */
    Page<CourseResponse> searchCourses(UUID categoryId, String search, Pageable pageable);

    /**
     * Retrieves courses created by a specific instructor.
     *
     * @param instructorId instructor ID
     * @param pageable     pagination info
     * @return paginated list of instructor's courses
     */
    Page<CourseResponse> getInstructorCourses(UUID instructorId, Pageable pageable);

    /**
     * Updates an existing course.
     *
     * @param courseId     the course ID
     * @param request      updated course data
     * @param instructorId instructor performing the update
     * @return updated course response
     */
    CourseResponse updateCourse(UUID courseId, CreateCourseRequest request, UUID instructorId);

    /**
     * Publishes a course.
     *
     * @param courseId     course ID
     * @param instructorId instructor ID
     * @return published course response
     */
    CourseResponse publishCourse(UUID courseId, UUID instructorId);

    /**
     * Retrieves most popular courses.
     *
     * @param limit number of courses to return
     * @return list of popular courses
     */
    List<CourseResponse> getPopularCourses(int limit);


    void updateCourseRating(UUID courseId, double newAverageRating, int totalReviews);

    void incrementEnrollmentCount(UUID courseId);
}
