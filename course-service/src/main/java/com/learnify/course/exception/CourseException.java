package com.learnify.course.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

public class CourseException {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class CourseNotFoundException extends RuntimeException {
        public CourseNotFoundException(UUID courseId) {
            super("Course not found with id: " + courseId);
        }
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class CategoryNotFoundException extends RuntimeException {
        public CategoryNotFoundException(UUID categoryId) {
            super("Category not found with id: " + categoryId);
        }
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    public static class UnauthorizedCourseAccessException extends RuntimeException {
        public UnauthorizedCourseAccessException(UUID courseId) {
            super("You are not authorized to modify course: " + courseId);
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class InvalidCourseStateException extends RuntimeException {
        public InvalidCourseStateException(String message) {
            super(message);
        }
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    public static class DuplicateCourseException extends RuntimeException {
        public DuplicateCourseException(String title) {
            super("A course with similar title already exists: " + title);
        }
    }
}
