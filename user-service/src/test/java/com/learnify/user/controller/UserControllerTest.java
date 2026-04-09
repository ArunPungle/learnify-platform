package com.learnify.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learnify.user.dto.request.UpdateProfileRequest;
import com.learnify.user.dto.response.UserProfileResponse;
import com.learnify.user.service.UserProfileService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserProfileService profileService;

    @Autowired
    private ObjectMapper objectMapper;

    private final UUID userId = UUID.randomUUID();

    private UserProfileResponse mockResponse() {
        return UserProfileResponse.builder()
                .id(userId)
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .bio("Bio")
                .headline("Headline")
                .avatarUrl("avatar.png")
                .role("STUDENT")
                .instructorVerified(true)
                .totalCourses(3)
                .totalStudents(50)
                .build();
    }

    // GET /api/users/me
    @Test
    void getMyProfile_shouldReturnProfile_whenHeaderPresent() throws Exception {
        Mockito.when(profileService.getProfile(userId))
                .thenReturn(mockResponse());

        mockMvc.perform(get("/api/users/me")
                        .header("X-User-Id", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.firstName").value("John"));

        Mockito.verify(profileService).getProfile(userId);
    }

    @Test
    void getMyProfile_shouldFail_whenHeaderMissing() throws Exception {
        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().isBadRequest());
    }

    // GET /api/users/{id}
    @Test
    void getProfile_shouldReturnProfile_whenValidId() throws Exception {
        Mockito.when(profileService.getProfile(userId))
                .thenReturn(mockResponse());

        mockMvc.perform(get("/api/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"));

        Mockito.verify(profileService).getProfile(userId);
    }

    // PUT /api/users/me
    @Test
    void updateProfile_shouldUpdateSuccessfully() throws Exception {
        UpdateProfileRequest request = new UpdateProfileRequest();
        request.setFirstName("Jane");

        Mockito.when(profileService.updateProfile(eq(userId), any()))
                .thenReturn(mockResponse());

        mockMvc.perform(put("/api/users/me")
                        .header("X-User-Id", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John")); // response mocked

        Mockito.verify(profileService)
                .updateProfile(eq(userId), any(UpdateProfileRequest.class));
    }

    @Test
    void updateProfile_shouldFail_whenHeaderMissing() throws Exception {
        UpdateProfileRequest request = new UpdateProfileRequest();

        mockMvc.perform(put("/api/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateProfile_shouldFail_whenInvalidJson() throws Exception {
        mockMvc.perform(put("/api/users/me")
                        .header("X-User-Id", userId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{invalid-json}"))
                .andExpect(status().isBadRequest());
    }
}