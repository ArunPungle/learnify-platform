package com.learnify.lesson.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learnify.lesson.dto.request.CreateLessonRequest;
import com.learnify.lesson.dto.request.CreateSectionRequest;
import com.learnify.lesson.dto.response.LessonResponse;
import com.learnify.lesson.dto.response.SectionResponse;
import com.learnify.lesson.service.LessonService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LessonController.class)
class LessonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LessonService lessonService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("Should create section successfully")
    void shouldCreateSection() throws Exception {
        CreateSectionRequest request = new CreateSectionRequest();
        SectionResponse response = new SectionResponse();

        Mockito.when(lessonService.createSection(Mockito.any()))
                .thenReturn(response);

        mockMvc.perform(post("/api/lessons/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(lessonService).createSection(Mockito.any());
        verifyNoMoreInteractions(lessonService);
    }


    @Test
    @DisplayName("Should return list of sections for a course")
    void shouldReturnCourseSections() throws Exception {
        UUID courseId = UUID.randomUUID();
        List<SectionResponse> sections = List.of(new SectionResponse());

        Mockito.when(lessonService.getCourseSections(courseId))
                .thenReturn(sections);

        mockMvc.perform(get("/api/lessons/sections/course/{courseId}", courseId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(sections)));

        verify(lessonService).getCourseSections(courseId);
        verifyNoMoreInteractions(lessonService);
    }

    @Test
    @DisplayName("Should return empty list when no sections exist")
    void shouldReturnEmptySectionsList() throws Exception {
        UUID courseId = UUID.randomUUID();

        Mockito.when(lessonService.getCourseSections(courseId))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/lessons/sections/course/{courseId}", courseId))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(lessonService).getCourseSections(courseId);
        verifyNoMoreInteractions(lessonService);
    }

    @Test
    @DisplayName("Should create lesson successfully")
    void shouldCreateLesson() throws Exception {
        CreateLessonRequest request = new CreateLessonRequest();
        LessonResponse response = new LessonResponse();

        Mockito.when(lessonService.createLesson(Mockito.any()))
                .thenReturn(response);

        mockMvc.perform(post("/api/lessons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(lessonService).createLesson(Mockito.any());
        verifyNoMoreInteractions(lessonService);
    }

    @Test
    @DisplayName("Should fail when lesson request is invalid")
    void shouldFailWhenCreateLessonRequestInvalid() throws Exception {
        CreateLessonRequest request = new CreateLessonRequest(); // invalid (empty)

        mockMvc.perform(post("/api/lessons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("Should return lesson by ID")
    void shouldReturnLessonById() throws Exception {
        UUID lessonId = UUID.randomUUID();
        LessonResponse response = new LessonResponse();

        Mockito.when(lessonService.getLessonById(lessonId))
                .thenReturn(response);

        mockMvc.perform(get("/api/lessons/{id}", lessonId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

        verify(lessonService).getLessonById(lessonId);
        verifyNoMoreInteractions(lessonService);
    }

    @Test
    void shouldGetLessonById() throws Exception {
        UUID lessonId = UUID.randomUUID();
        Mockito.when(lessonService.getLessonById(lessonId))
                .thenReturn(new LessonResponse());

        mockMvc.perform(get("/api/lessons/{id}", lessonId))
                .andExpect(status().isOk());
    }
}