package com.learnify.lesson.service;

import com.learnify.lesson.dto.request.CreateLessonRequest;
import com.learnify.lesson.dto.request.CreateSectionRequest;
import com.learnify.lesson.dto.response.LessonResponse;
import com.learnify.lesson.dto.response.SectionResponse;
import com.learnify.lesson.entity.Lesson;
import com.learnify.lesson.entity.LessonType;
import com.learnify.lesson.entity.Section;
import com.learnify.lesson.exception.LessonNotFoundException;
import com.learnify.lesson.repository.LessonRepository;
import com.learnify.lesson.repository.SectionRepository;
import com.learnify.lesson.service.Impl.LessonServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LessonServiceImplTest {

    @Mock
    private SectionRepository sectionRepository;

    @Mock
    private LessonRepository lessonRepository;

    @InjectMocks
    private LessonServiceImpl lessonService;

    private Section section;
    private Lesson lesson;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        section = Section.builder()
                .id(UUID.randomUUID())
                .courseId(UUID.randomUUID())
                .title("Section 1")
                .orderIndex(1)
                .build();

        lesson = Lesson.builder()
                .id(UUID.randomUUID())
                .section(section)
                .title("Lesson 1")
                .description("Lesson Description")
                .type(LessonType.VIDEO)
                .videoUrl("https://video.url")
                .videoDurationSeconds(120)
                .contentText("Content Text")
                .orderIndex(1)
                .freePreview(true)
                .createdAt(LocalDateTime.now())
                .build();

        section.setLessons(List.of(lesson));
    }

    // Section Tests
    @Test
    void testCreateSection() {
        CreateSectionRequest request = CreateSectionRequest.builder()
                .courseId(section.getCourseId())
                .title(section.getTitle())
                .orderIndex(section.getOrderIndex())
                .build();

        when(sectionRepository.save(any(Section.class))).thenReturn(section);

        SectionResponse response = lessonService.createSection(request);

        assertNotNull(response);
        assertEquals(section.getTitle(), response.getTitle());
        verify(sectionRepository, times(1)).save(any(Section.class));
    }

    @Test
    void testCreateSection_NullTitle() {
        CreateSectionRequest request = CreateSectionRequest.builder()
                .courseId(UUID.randomUUID())
                .title(null)
                .orderIndex(1)
                .build();

        when(sectionRepository.save(any(Section.class))).thenAnswer(invocation -> {
            Section s = invocation.getArgument(0);
            s.setId(UUID.randomUUID());
            return s;
        });

        SectionResponse response = lessonService.createSection(request);

        assertNotNull(response);
        assertNull(response.getTitle()); // title remains null
    }

    @Test
    void testGetCourseSections() {
        UUID courseId = section.getCourseId();
        when(sectionRepository.findByCourseIdOrderByOrderIndex(courseId))
                .thenReturn(List.of(section));

        List<SectionResponse> responses = lessonService.getCourseSections(courseId);

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(section.getTitle(), responses.get(0).getTitle());
        verify(sectionRepository, times(1)).findByCourseIdOrderByOrderIndex(courseId);
    }

    @Test
    void testGetCourseSections_Empty() {
        UUID courseId = UUID.randomUUID();
        when(sectionRepository.findByCourseIdOrderByOrderIndex(courseId))
                .thenReturn(List.of());

        List<SectionResponse> responses = lessonService.getCourseSections(courseId);

        assertNotNull(responses);
        assertTrue(responses.isEmpty());
    }

    @Test
    void testSectionResponseContainsMappedLessons() {
        UUID courseId = section.getCourseId();
        when(sectionRepository.findByCourseIdOrderByOrderIndex(courseId))
                .thenReturn(List.of(section));

        List<SectionResponse> responses = lessonService.getCourseSections(courseId);

        assertEquals(1, responses.size());
        SectionResponse sectionResponse = responses.get(0);
        assertEquals(1, sectionResponse.getLessons().size());
        assertEquals(lesson.getTitle(), sectionResponse.getLessons().get(0).getTitle());
    }

    // Lesson Tests
    @Test
    void testCreateLesson_Success() {
        CreateLessonRequest request = CreateLessonRequest.builder()
                .sectionId(section.getId())
                .title(lesson.getTitle())
                .description(lesson.getDescription())
                .type(lesson.getType())
                .videoUrl(lesson.getVideoUrl())
                .videoDurationSeconds(lesson.getVideoDurationSeconds())
                .contentText(lesson.getContentText())
                .orderIndex(lesson.getOrderIndex())
                .freePreview(lesson.isFreePreview())
                .build();

        when(sectionRepository.findById(section.getId())).thenReturn(Optional.of(section));
        when(lessonRepository.save(any(Lesson.class))).thenReturn(lesson);

        LessonResponse response = lessonService.createLesson(request);

        assertNotNull(response);
        assertEquals(lesson.getTitle(), response.getTitle());
        verify(lessonRepository, times(1)).save(any(Lesson.class));
    }

    @Test
    void testCreateLesson_SectionNotFound() {
        CreateLessonRequest request = CreateLessonRequest.builder()
                .sectionId(UUID.randomUUID())
                .build();

        when(sectionRepository.findById(request.getSectionId())).thenReturn(Optional.empty());

        assertThrows(LessonNotFoundException.class, () -> lessonService.createLesson(request));
    }

    @Test
    void testCreateLesson_TextOnlyLesson() {
        CreateLessonRequest request = CreateLessonRequest.builder()
                .sectionId(section.getId())
                .title("Text Lesson")
                .description("Description")
                .type(LessonType.TEXT)
                .videoUrl(null)
                .videoDurationSeconds(null)
                .contentText("This is a text lesson")
                .orderIndex(2)
                .freePreview(false)
                .build();

        when(sectionRepository.findById(section.getId())).thenReturn(Optional.of(section));

        Lesson textLesson = Lesson.builder()
                .id(UUID.randomUUID())
                .section(section)
                .title(request.getTitle())
                .description(request.getDescription())
                .type(request.getType())
                .videoUrl(null)
                .videoDurationSeconds(null)
                .contentText(request.getContentText())
                .orderIndex(request.getOrderIndex())
                .freePreview(false)
                .createdAt(LocalDateTime.now())
                .build();

        when(lessonRepository.save(any(Lesson.class))).thenReturn(textLesson);

        LessonResponse response = lessonService.createLesson(request);

        assertNotNull(response);
        assertEquals(request.getTitle(), response.getTitle());
        assertNull(response.getVideoUrl());
        assertFalse(response.isFreePreview());
    }

    @Test
    void testGetLessonById_Success() {
        when(lessonRepository.findById(lesson.getId())).thenReturn(Optional.of(lesson));

        LessonResponse response = lessonService.getLessonById(lesson.getId());

        assertNotNull(response);
        assertEquals(lesson.getTitle(), response.getTitle());
        verify(lessonRepository, times(1)).findById(lesson.getId());
    }

    @Test
    void testGetLessonById_NotFound() {
        UUID lessonId = UUID.randomUUID();
        when(lessonRepository.findById(lessonId)).thenReturn(Optional.empty());

        assertThrows(LessonNotFoundException.class, () -> lessonService.getLessonById(lessonId));
    }
}