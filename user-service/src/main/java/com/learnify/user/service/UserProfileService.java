package com.learnify.user.service;

import com.learnify.user.dto.request.UpdateProfileRequest;
import com.learnify.user.dto.response.UserProfileResponse;
import com.learnify.user.exception.UserNotFoundException;

import java.util.UUID;

public interface UserProfileService {

    /**
     * Retrieves the profile details of a user by their unique identifier.
     *
     * @param userId the unique ID of the user whose profile is to be fetched
     * @return the user profile information wrapped in a UserProfileResponse
     * @throws UserNotFoundException if no user exists with the given ID
     */
    UserProfileResponse getProfile(UUID userId);

    /**
     * Updates the profile details of a user with the provided information.
     *
     * @param userId the unique ID of the user whose profile is to be updated
     * @param request the request object containing updated profile fields
     * @return the updated user profile information wrapped in a UserProfileResponse
     */
    UserProfileResponse updateProfile(UUID userId, UpdateProfileRequest request);
}
