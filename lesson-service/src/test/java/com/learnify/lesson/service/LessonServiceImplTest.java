package com.learnify.lesson.service;

import com.learnify.lesson.dto.request.CreateLessonRequest;
import com.learnify.lesson.dto.request.CreateSectionRequest;
import com.learnify.lesson.dto.response.LessonResponse;
import com.learnify.lesson.dto.response.SectionResponse;
import com.learnify.lesson.entity.Lesson;
import com.learnify.lesson.entity.Section;
import com.learnify.lesson.exception.LessonNotFoundException;
import com.learnify.lesson.repository.LessonRepository;
import com.learnify.lesson.repository.SectionRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class LessonServiceImplTest {

    @Mock
    private SectionRepository sectionRepository;

    @Mock
    private LessonRepository lessonRepository;

    @InjectMocks
    private LessonService lessonService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    @DisplayName("Should create section successfully")
    void shouldCreateSection() {
        UUID courseId = UUID.randomUUID();

        CreateSectionRequest request = new CreateSectionRequest();
        request.setCourseId(courseId);
        request.setTitle("Section 1");
        request.setOrderIndex(1);

        Section savedSection = Section.builder()
                .id(UUID.randomUUID())
                .courseId(courseId)
                .title("Section 1")
                .orderIndex(1)
                .lessons(List.of())
                .build();

        when(sectionRepository.save(any())).thenReturn(savedSection);

        SectionResponse response = lessonService.createSection(request);

        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo("Section 1");

        verify(sectionRepository).save(any());
        verifyNoMoreInteractions(sectionRepository);
    }


    @Test
    @DisplayName("Should return sections for a course")
    void shouldReturnCourseSections() {
        UUID courseId = UUID.randomUUID();

        Section section = Section.builder()
                .id(UUID.randomUUID())
                .courseId(courseId)
                .title("Section 1")
                .orderIndex(1)
                .lessons(List.of())
                .build();

        when(sectionRepository.findByCourseIdOrderByOrderIndex(courseId))
                .thenReturn(List.of(section));

        List<SectionResponse> result = lessonService.getCourseSections(courseId);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Section 1");

        verify(sectionRepository).findByCourseIdOrderByOrderIndex(courseId);
        verifyNoMoreInteractions(sectionRepository);
    }

    @Test
    @DisplayName("Should return empty list when no sections found")
    void shouldReturnEmptySections() {
        UUID courseId = UUID.randomUUID();

        when(sectionRepository.findByCourseIdOrderByOrderIndex(courseId))
                .thenReturn(List.of());

        List<SectionResponse> result = lessonService.getCourseSections(courseId);

        assertThat(result).isEmpty();

        verify(sectionRepository).findByCourseIdOrderByOrderIndex(courseId);
    }


    @Test
    @DisplayName("Should create lesson successfully")
    void shouldCreateLesson() {
        UUID sectionId = UUID.randomUUID();

        Section section = Section.builder()
                .id(sectionId)
                .build();

        CreateLessonRequest request = new CreateLessonRequest();
        request.setSectionId(sectionId);
        request.setTitle("Lesson 1");
        request.setDescription("Desc");
        request.setOrderIndex(1);
        request.setFreePreview(true);

        Lesson savedLesson = Lesson.builder()
                .id(UUID.randomUUID())
                .section(section)
                .title("Lesson 1")
                .description("Desc")
                .orderIndex(1)
                .freePreview(true)
                .build();

        when(sectionRepository.findById(sectionId)).thenReturn(Optional.of(section));
        when(lessonRepository.save(any())).thenReturn(savedLesson);

        LessonResponse response = lessonService.createLesson(request);

        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo("Lesson 1");

        verify(sectionRepository).findById(sectionId);
        verify(lessonRepository).save(any());
        verifyNoMoreInteractions(sectionRepository, lessonRepository);
    }

    @Test
    @DisplayName("Should throw exception when section not found")
    void shouldThrowWhenSectionNotFound() {
        UUID sectionId = UUID.randomUUID();

        CreateLessonRequest request = new CreateLessonRequest();
        request.setSectionId(sectionId);

        when(sectionRepository.findById(sectionId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> lessonService.createLesson(request))
                .isInstanceOf(LessonNotFoundException.class)
                .hasMessage("Section not found");

        verify(sectionRepository).findById(sectionId);
        verifyNoMoreInteractions(sectionRepository);
    }


    @Test
    @DisplayName("Should return lesson by ID")
    void shouldReturnLessonById() {
        UUID lessonId = UUID.randomUUID();

        Section section = Section.builder()
                .id(UUID.randomUUID())
                .build();

        Lesson lesson = Lesson.builder()
                .id(lessonId)
                .section(section)
                .title("Lesson 1")
                .build();

        when(lessonRepository.findById(lessonId))
                .thenReturn(Optional.of(lesson));

        LessonResponse response = lessonService.getLessonById(lessonId);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(lessonId);

        verify(lessonRepository).findById(lessonId);
        verifyNoMoreInteractions(lessonRepository);
    }

    @Test
    @DisplayName("Should throw exception when lesson not found")
    void shouldThrowWhenLessonNotFound() {
        UUID lessonId = UUID.randomUUID();

        when(lessonRepository.findById(lessonId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> lessonService.getLessonById(lessonId))
                .isInstanceOf(LessonNotFoundException.class)
                .hasMessageContaining("Lesson not found");

        verify(lessonRepository).findById(lessonId);
    }
}