package com.learnify.user.service;

import com.learnify.user.dto.request.UpdateProfileRequest;
import com.learnify.user.dto.response.UserProfileResponse;
import com.learnify.user.entity.UserProfile;
import com.learnify.user.entity.UserRole;
import com.learnify.user.exception.UserNotFoundException;
import com.learnify.user.repository.UserProfileRepository;
import com.learnify.user.service.impl.UserProfileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserProfileServiceImplTest {

    @Mock
    private UserProfileRepository userProfileRepository;

    @InjectMocks
    private UserProfileServiceImpl userProfileService;

    private UUID userId;
    private UserProfile userProfile;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();

        userProfile = new UserProfile();
        userProfile.setId(userId);
        userProfile.setEmail("test@example.com");
        userProfile.setFirstName("John");
        userProfile.setLastName("Doe");
        userProfile.setBio("Old bio");
        userProfile.setHeadline("Old headline");
        userProfile.setAvatarUrl("old-avatar.png");
        userProfile.setInstructorVerified(true);
        userProfile.setTotalCourses(5);
        userProfile.setTotalStudents(100);
        userProfile.setUpdatedAt(LocalDateTime.now());
        userProfile.setRole(UserRole.STUDENT);
    }


    // getProfile Tests
    @Test
    void getProfile_shouldReturnUserProfileResponse_whenUserExists() {
        when(userProfileRepository.findById(userId))
                .thenReturn(Optional.of(userProfile));

        UserProfileResponse response = userProfileService.getProfile(userId);

        assertNotNull(response);
        assertEquals(userId, response.getId());
        assertEquals("John", response.getFirstName());
        assertEquals("Doe", response.getLastName());
        assertEquals("test@example.com", response.getEmail());

        verify(userProfileRepository, times(1)).findById(userId);
    }

    @Test
    void getProfile_shouldThrowException_whenUserNotFound() {
        when(userProfileRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userProfileService.getProfile(userId));

        verify(userProfileRepository, times(1)).findById(userId);
    }

    // updateProfile Tests
    @Test
    void updateProfile_shouldUpdateAllFields_whenRequestHasValues() {
        UpdateProfileRequest request = new UpdateProfileRequest();
        request.setFirstName("Jane");
        request.setLastName("Smith");
        request.setBio("New bio");
        request.setHeadline("New headline");
        request.setAvatarUrl("new-avatar.png");

        when(userProfileRepository.findById(userId))
                .thenReturn(Optional.of(userProfile));

        when(userProfileRepository.save(any(UserProfile.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UserProfileResponse response =
                userProfileService.updateProfile(userId, request);

        assertEquals("Jane", response.getFirstName());
        assertEquals("Smith", response.getLastName());
        assertEquals("New bio", response.getBio());
        assertEquals("New headline", response.getHeadline());
        assertEquals("new-avatar.png", response.getAvatarUrl());

        verify(userProfileRepository).save(userProfile);
    }

    @Test
    void updateProfile_shouldUpdateOnlyNonNullFields() {
        UpdateProfileRequest request = new UpdateProfileRequest();
        request.setFirstName("UpdatedName"); // only one field

        when(userProfileRepository.findById(userId))
                .thenReturn(Optional.of(userProfile));

        when(userProfileRepository.save(any(UserProfile.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UserProfileResponse response =
                userProfileService.updateProfile(userId, request);

        assertEquals("UpdatedName", response.getFirstName());

        // unchanged fields
        assertEquals("Doe", response.getLastName());
        assertEquals("Old bio", response.getBio());

        verify(userProfileRepository).save(userProfile);
    }

    @Test
    void updateProfile_shouldThrowException_whenUserNotFound() {
        UpdateProfileRequest request = new UpdateProfileRequest();

        when(userProfileRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userProfileService.updateProfile(userId, request));

        verify(userProfileRepository, never()).save(any());
    }

    @Test
    void updateProfile_shouldUpdateTimestamp() {
        UpdateProfileRequest request = new UpdateProfileRequest();
        request.setFirstName("TimeTest");

        when(userProfileRepository.findById(userId))
                .thenReturn(Optional.of(userProfile));

        when(userProfileRepository.save(any(UserProfile.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        LocalDateTime beforeUpdate = userProfile.getUpdatedAt();

        userProfileService.updateProfile(userId, request);

        assertTrue(userProfile.getUpdatedAt().isAfter(beforeUpdate));
    }
}