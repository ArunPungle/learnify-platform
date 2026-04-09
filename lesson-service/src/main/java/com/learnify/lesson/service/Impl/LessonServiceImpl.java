package com.learnify.lesson.service.Impl;

import com.learnify.lesson.dto.request.CreateLessonRequest;
import com.learnify.lesson.dto.request.CreateSectionRequest;
import com.learnify.lesson.dto.response.LessonResponse;
import com.learnify.lesson.dto.response.SectionResponse;
import com.learnify.lesson.entity.Lesson;
import com.learnify.lesson.entity.Section;
import com.learnify.lesson.exception.LessonNotFoundException;
import com.learnify.lesson.repository.LessonRepository;
import com.learnify.lesson.repository.SectionRepository;
import com.learnify.lesson.service.LessonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {

    private final SectionRepository sectionRepository;
    private final LessonRepository lessonRepository;

    @Transactional
    @Override
    public SectionResponse createSection(CreateSectionRequest request) {
        Section section = Section.builder()
                .courseId(request.getCourseId())
                .title(request.getTitle())
                .orderIndex(request.getOrderIndex())
                .build();
        section = sectionRepository.save(section);
        return mapSectionToResponse(section);
    }

    @Transactional(readOnly = true)
    @Override
    public List<SectionResponse> getCourseSections(UUID courseId) {
        return sectionRepository.findByCourseIdOrderByOrderIndex(courseId)
                .stream()
                .map(this::mapSectionToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public LessonResponse createLesson(CreateLessonRequest request) {
        Section section = sectionRepository.findById(request.getSectionId())
                .orElseThrow(() -> new LessonNotFoundException("Section not found"));

        Lesson lesson = Lesson.builder()
                .section(section)
                .title(request.getTitle())
                .description(request.getDescription())
                .type(request.getType())
                .videoUrl(request.getVideoUrl())
                .videoDurationSeconds(request.getVideoDurationSeconds())
                .contentText(request.getContentText())
                .orderIndex(request.getOrderIndex())
                .freePreview(request.isFreePreview())
                .createdAt(LocalDateTime.now())
                .build();

        return mapLessonToResponse(lessonRepository.save(lesson));
    }

    @Transactional(readOnly = true)
    @Override
    public LessonResponse getLessonById(UUID id) {
        return lessonRepository.findById(id)
                .map(this::mapLessonToResponse)
                .orElseThrow(() -> new LessonNotFoundException("Lesson not found: " + id));
    }

    private SectionResponse mapSectionToResponse(Section s) {
        return SectionResponse.builder()
                .id(s.getId())
                .courseId(s.getCourseId())
                .title(s.getTitle())
                .orderIndex(s.getOrderIndex())
                .lessons(s.getLessons().stream().map(this::mapLessonToResponse).collect(Collectors.toList()))
                .build();
    }

    private LessonResponse mapLessonToResponse(Lesson l) {
        return LessonResponse.builder()
                .id(l.getId())
                .sectionId(l.getSection().getId())
                .title(l.getTitle())
                .description(l.getDescription())
                .type(l.getType().name())
                .videoUrl(l.getVideoUrl())
                .videoDurationSeconds(l.getVideoDurationSeconds())
                .contentText(l.getContentText())
                .orderIndex(l.getOrderIndex())
                .freePreview(l.isFreePreview())
                .build();
    }

}
