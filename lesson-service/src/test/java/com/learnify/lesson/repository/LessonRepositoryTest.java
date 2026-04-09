package com.learnify.lesson.repository;

import com.learnify.lesson.entity.Lesson;
import com.learnify.lesson.entity.Section;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class LessonRepositoryTest {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Test
    @DisplayName("Should return lessons ordered by orderIndex for a section")
    void shouldFindLessonsBySectionIdOrdered() {
        // Create Section
        Section section = Section.builder()
                .title("Section 1")
                .courseId(UUID.randomUUID())
                .orderIndex(1)
                .build();
        sectionRepository.save(section);

        // Create Lessons
        Lesson lesson1 = Lesson.builder()
                .section(section)
                .orderIndex(2)
                .build();

        Lesson lesson2 = Lesson.builder()
                .section(section)
                .orderIndex(1)
                .build();

        lessonRepository.saveAll(List.of(lesson1, lesson2));

        List<Lesson> result = lessonRepository
                .findBySectionIdOrderByOrderIndex(section.getId());

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getOrderIndex()).isEqualTo(1);
        assertThat(result.get(1).getOrderIndex()).isEqualTo(2);
    }

    @Test
    @DisplayName("Should return empty list when no lessons found")
    void shouldReturnEmptyListWhenNoLessons() {
        UUID sectionId = UUID.randomUUID();

        List<Lesson> result = lessonRepository
                .findBySectionIdOrderByOrderIndex(sectionId);

        assertThat(result).isEmpty();
    }
}