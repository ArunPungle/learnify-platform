package com.learnify.lesson.repository;

import com.learnify.lesson.entity.Lesson;
import com.learnify.lesson.entity.Section;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class LessonRepositoryTest {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private EntityManager entityManager;


    @Test
    @DisplayName("Should return lessons ordered by orderIndex")
    void shouldFindLessonsBySectionIdOrdered() {
        UUID sectionId = UUID.randomUUID();

        Section section = Section.builder()
                .id(sectionId)
                .title("Section 1")
                .build();

        entityManager.persist(section);

        Lesson lesson1 = Lesson.builder()
                .id(UUID.randomUUID())
                .section(section)
                .title("Lesson 1")
                .orderIndex(2)
                .build();

        Lesson lesson2 = Lesson.builder()
                .id(UUID.randomUUID())
                .section(section)
                .title("Lesson 2")
                .orderIndex(1)
                .build();

        entityManager.persist(lesson1);
        entityManager.persist(lesson2);
        entityManager.flush();

        List<Lesson> result = lessonRepository.findBySectionIdOrderByOrderIndex(sectionId);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getOrderIndex()).isEqualTo(1);
        assertThat(result.get(1).getOrderIndex()).isEqualTo(2);
    }

    @Test
    @DisplayName("Should return empty list when no lessons exist")
    void shouldReturnEmptyList() {
        UUID sectionId = UUID.randomUUID();
        List<Lesson> result = lessonRepository.findBySectionIdOrderByOrderIndex(sectionId);
        assertThat(result).isEmpty();
    }
}