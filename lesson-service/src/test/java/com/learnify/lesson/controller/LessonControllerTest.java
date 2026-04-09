package com.learnify.lesson.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learnify.lesson.dto.request.CreateLessonRequest;
import com.learnify.lesson.dto.request.CreateSectionRequest;
import com.learnify.lesson.dto.response.LessonResponse;
import com.learnify.lesson.dto.response.SectionResponse;
import com.learnify.lesson.service.LessonService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

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
    void shouldCreateSection() throws Exception {
        CreateSectionRequest request = new CreateSectionRequest();
        SectionResponse response = new SectionResponse();

        Mockito.when(lessonService.createSection(Mockito.any()))
                .thenReturn(response);

        mockMvc.perform(post("/api/lessons/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldGetCourseSections() throws Exception {
        UUID courseId = UUID.randomUUID();
        Mockito.when(lessonService.getCourseSections(courseId))
                .thenReturn(List.of(new SectionResponse()));

        mockMvc.perform(get("/api/lessons/sections/course/{courseId}", courseId))
                .andExpect(status().isOk());
    }

    @Test
    void shouldCreateLesson() throws Exception {
        CreateLessonRequest request = new CreateLessonRequest();
        LessonResponse response = new LessonResponse();

        Mockito.when(lessonService.createLesson(Mockito.any()))
                .thenReturn(response);

        mockMvc.perform(post("/api/lessons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
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