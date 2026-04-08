package com.learnify.user.service;

import com.learnify.user.dto.request.UpdateProfileRequest;
import com.learnify.user.dto.response.UserProfileResponse;

import java.util.UUID;

public interface UserProfileService {
    UserProfileResponse getProfile(UUID userId);

    UserProfileResponse updateProfile(UUID userId, UpdateProfileRequest request);
}
