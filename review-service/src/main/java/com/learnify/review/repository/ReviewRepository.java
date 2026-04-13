package com.learnify.review.repository;

import com.learnify.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {

    Optional<Review> findByStudentIdAndCourseId(UUID studentId, UUID courseId);

    boolean existsByStudentIdAndCourseId(UUID studentId, UUID courseId);

    Page<Review> findByCourseIdAndVisible(UUID courseId, boolean visible, Pageable pageable);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.courseId = :courseId AND r.visible = true")
    Double getAverageRatingByCourseId(UUID courseId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.courseId = :courseId AND r.visible = true")
    long countByCourseId(UUID courseId);
}
