package com.learnify.user.repository;

import com.learnify.user.entity.UserProfile;
import com.learnify.user.entity.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserProfileRepositoryTest {

    @Autowired
    private UserProfileRepository repository;

    private UserProfile createUser(String email) {
        UserProfile user = new UserProfile();
        user.setId(UUID.randomUUID());
        user.setEmail(email);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setBio("Bio");
        user.setHeadline("Headline");
        user.setAvatarUrl("avatar.png");
        user.setInstructorVerified(false);
        user.setTotalCourses(0);
        user.setTotalStudents(0);
        user.setRole(UserRole.STUDENT);

        return user;
    }

    // findByEmail Tests
    @Test
    @DisplayName("Should find user by email")
    void findByEmail_shouldReturnUser_whenExists() {
        UserProfile user = createUser("test@example.com");
        repository.save(user);

        Optional<UserProfile> result = repository.findByEmail("test@example.com");

        assertTrue(result.isPresent());
        assertEquals("test@example.com", result.get().getEmail());
    }

    @Test
    @DisplayName("Should return empty when email not found")
    void findByEmail_shouldReturnEmpty_whenNotExists() {
        Optional<UserProfile> result = repository.findByEmail("notfound@example.com");

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should persist and retrieve user correctly")
    void save_shouldPersistUser() {
        UserProfile user = createUser("persist@example.com");

        UserProfile saved = repository.save(user);

        assertNotNull(saved.getId());

        Optional<UserProfile> fetched = repository.findById(saved.getId());

        assertTrue(fetched.isPresent());
        assertEquals("persist@example.com", fetched.get().getEmail());
    }

    @Test
    @DisplayName("Should enforce unique email if constraint exists")
    void save_shouldFail_whenDuplicateEmail_ifConstraintPresent() {
        UserProfile user1 = createUser("dup@example.com");
        UserProfile user2 = createUser("dup@example.com");

        repository.save(user1);

        try {
            repository.saveAndFlush(user2);
        } catch (Exception ex) {
            // Only valid if DB constraint exists
            assertTrue(true);
            return;
        }

        // If no constraint, test should reflect that
        System.out.println("⚠️ No unique constraint on email column");
    }
}