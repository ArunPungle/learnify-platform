package com.learnify.user.controller;

import com.learnify.user.repository.UserProfileRepository;
import com.learnify.user.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing users.
 *
 * Provides APIs for CRUD operations on UserProfile.
 *
 * @author Arun Pungale
 */

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserProfileService userService;
    private final UserProfileRepository userRepository;


}
