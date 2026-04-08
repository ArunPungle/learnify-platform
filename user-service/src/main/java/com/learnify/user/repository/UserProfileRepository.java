package com.learnify.user.repository;

import com.learnify.user.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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
}
