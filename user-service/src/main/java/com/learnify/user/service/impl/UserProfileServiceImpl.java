package com.learnify.user.service.impl;

import com.learnify.user.dto.request.UpdateProfileRequest;
import com.learnify.user.dto.response.UserProfileResponse;
import com.learnify.user.entity.UserProfile;
import com.learnify.user.exception.UserNotFoundException;
import com.learnify.user.repository.UserProfileRepository;
import com.learnify.user.service.UserProfileService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;

    @Cacheable(value = "userProfiles", key = "#id")
    @Transactional(readOnly = true)
    @Override
    public UserProfileResponse getProfile(UUID id) {
        return userProfileRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + id));
    }

    @CacheEvict(value = "userProfiles", key = "#id")
    @Transactional
    @Override
    public UserProfileResponse updateProfile(UUID id, UpdateProfileRequest request) {
        UserProfile profile = userProfileRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + id));

        if (request.getFirstName() != null)
            profile.setFirstName(request.getFirstName());
        if (request.getLastName() != null)
            profile.setLastName(request.getLastName());
        if (request.getBio() != null)
            profile.setBio(request.getBio());
        if (request.getHeadline() != null)
            profile.setHeadline(request.getHeadline());
        if (request.getAvatarUrl() != null)
            profile.setAvatarUrl(request.getAvatarUrl());

        profile.setUpdatedAt(LocalDateTime.now());

        return mapToResponse(userProfileRepository.save(profile));
    }

    private UserProfileResponse mapToResponse(UserProfile p) {
        return UserProfileResponse.builder()
                .id(p.getId())
                .email(p.getEmail())
                .firstName(p.getFirstName())
                .lastName(p.getLastName())
                .avatarUrl(p.getAvatarUrl())
                .bio(p.getBio())
                .headline(p.getHeadline())
                .role(p.getRole().name())
                .instructorVerified(p.isInstructorVerified())
                .totalStudents(p.getTotalStudents())
                .totalCourses(p.getTotalCourses())
                .build();
    }
}
