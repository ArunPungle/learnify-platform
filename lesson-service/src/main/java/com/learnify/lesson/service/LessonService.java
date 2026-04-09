package com.learnify.lesson.service;

import com.learnify.lesson.dto.request.CreateLessonRequest;
import com.learnify.lesson.dto.request.CreateSectionRequest;
import com.learnify.lesson.dto.response.LessonResponse;
import com.learnify.lesson.dto.response.SectionResponse;
import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public interface LessonService {

    /**
     * Creates a new section for a course.
     *
     * @param request the {@link CreateSectionRequest} containing section details; must be valid
     * @return the created {@link SectionResponse}, or null if creation fails
     */
    @Nullable
    SectionResponse createSection(@Valid CreateSectionRequest request);


    /**
     * Retrieves all sections associated with a given course.
     *
     * @param courseId the unique identifier of the course
     * @return a list of {@link SectionResponse} objects, or null if no sections are found
     */
    @Nullable
    List<SectionResponse> getCourseSections(UUID courseId);


    /**
     * Creates a new lesson within a section or course.
     *
     * @param request the {@link CreateLessonRequest} containing lesson details; must be valid
     * @return the created {@link LessonResponse}, or null if creation fails
     */
    @Nullable
    LessonResponse createLesson(@Valid CreateLessonRequest request);


    /**
     * Retrieves a lesson by its unique identifier.
     *
     * @param id the unique identifier of the lesson
     * @return the {@link LessonResponse} if found, or null if no lesson exists with the given ID
     */
    @Nullable
    LessonResponse getLessonById(UUID id);
}
