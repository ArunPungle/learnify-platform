package com.learnify.user.repository;

import com.learnify.user.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for UserProfile entity.
 *
 * Provides CRUD operations and custom query methods for user management.
 *
 * @author Arun Pungale
 */
@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {

    Optional<UserProfile> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<UserProfile> findByUsername(String username);

    boolean existsByUsername(String username);

    List<UserProfile> findByStatus(String status);

    List<UserProfile> findByCreatedAtAfter(LocalDateTime dateTime);

    List<UserProfile> findByRole(String role);

}
