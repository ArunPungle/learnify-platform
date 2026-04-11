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

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

class LessonServiceImplTest {

    @Mock
    private SectionRepository sectionRepository;

    @Mock
    private LessonRepository lessonRepository;

    @InjectMocks
    private LessonService lessonService;

    private Section section;
    private Lesson lesson;

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