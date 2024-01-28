package com.zoola.tutorial.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zoola.tutorial.exception.ControllerExceptionHandler;
import com.zoola.tutorial.model.Tutorial;
import com.zoola.tutorial.service.TutorialService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({TutorialController.class})
public class TutorialControllerTests {

    @MockBean
    private TutorialService tutorialService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should return list of tutorials")
    public void shouldListOfTutorials() throws Exception {
        final List<Tutorial> tutorials = List.of(
                new Tutorial(1L, "Tutorial 1", "Description 1", true),
                new Tutorial(2L, "Tutorial 2", "Description 2", false)
        );

        when(tutorialService.getAllTutorials(null)).thenReturn(tutorials);

        mockMvc.perform(get("/api/tutorials"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(tutorials)))
                .andExpect(jsonPath("$.size()").value(tutorials.size()));
    }

    @Test
    @DisplayName("Should return no content when no tutorials found")
    public void shouldReturnNoContentWhenNoTutorialsFound() throws Exception {
        when(tutorialService.getAllTutorials(null)).thenReturn(List.of());

        mockMvc.perform(get("/api/tutorials"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should return tutorial by id")
    public void shouldReturnTutorialById() throws Exception {
        final long id = 1L;
        final Tutorial tutorial = new Tutorial(id, "Tutorial 1", "Description 1", true);

        when(tutorialService.getTutorialById(id)).thenReturn(tutorial);

        mockMvc.perform(get("/api/tutorials/" + id))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(tutorial)));
    }

    @Test
    @DisplayName("Should create tutorial")
    public void shouldCreateTutorial() throws Exception {
        final Tutorial tutorial = new Tutorial(1L, "Tutorial 1", "Description 1", true);

        when(tutorialService.createTutorial(tutorial)).thenReturn(tutorial);

        mockMvc.perform(post("/api/tutorials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tutorial)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Should update tutorial")
    public void shouldUpdateTutorial() throws Exception {
        final long id = 1L;
        final Tutorial tutorial = new Tutorial(id, "Tutorial 1", "Description 1", true);

        when(tutorialService.updateTutorial(id, tutorial)).thenReturn(tutorial);

        mockMvc.perform(put("/api/tutorials/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tutorial)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(tutorial)));
    }

    @Test
    @DisplayName("Should delete tutorial by id")
    public void shouldDeleteTutorialById() throws Exception {
        final long id = 1L;

        mockMvc.perform(delete("/api/tutorials/" + id))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should delete all tutorials")
    public void shouldDeleteAllTutorials() throws Exception {

        mockMvc.perform(delete("/api/tutorials"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should return list of published tutorials")
    public void shouldReturnListOfPublishedTutorials() throws Exception {
        final List<Tutorial> tutorials = List.of(
                new Tutorial(1L, "Tutorial 1", "Description 1", true),
                new Tutorial(2L, "Tutorial 2", "Description 2", true)
        );

        when(tutorialService.findByPublished()).thenReturn(tutorials);

        mockMvc.perform(get("/api/tutorials/published"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(tutorials)))
                .andExpect(jsonPath("$.size()").value(tutorials.size()));
    }
}
