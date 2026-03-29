package com.learnify.course.repository;

import com.learnify.course.entity.Course;
import com.learnify.course.entity.CourseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {

    Page<Course> findByStatus(CourseStatus status, Pageable pageable);

    Page<Course> findByInstructorId(UUID instructorId, Pageable pageable);

    @Query("""
            SELECT c FROM Course c
            WHERE c.status = 'PUBLISHED'
            AND (:categoryId IS NULL OR c.category.id = :categoryId)
            AND (:search IS NULL OR LOWER(c.title) LIKE LOWER(CONCAT('%', :search, '%'))
                 OR LOWER(c.description) LIKE LOWER(CONCAT('%', :search, '%')))
            """)
    Page<Course> searchCourses(
            @Param("categoryId") UUID categoryId,
            @Param("search") String search,
            Pageable pageable);

    @Query("SELECT c FROM Course c WHERE c.status = 'PUBLISHED' ORDER BY c.totalEnrollments DESC")
    List<Course> findTopPopularCourses(Pageable pageable);

    List<Course> findByIdIn(List<UUID> ids);
}
