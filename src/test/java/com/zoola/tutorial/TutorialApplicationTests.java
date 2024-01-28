package com.zoola.tutorial;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zoola.tutorial.model.Tutorial;
import com.zoola.tutorial.repository.TutorialRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TutorialApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TutorialRepository tutorialRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void contextLoads() {
    }

    @BeforeEach
    void setUp() {
        tutorialRepository.deleteAll();
    }

    @Test
    @DisplayName("Should find no tutorials if repository is empty")
    void itShouldReturnNoContentWhenNoTutorialsAreInDb() throws Exception {
        mockMvc.perform(get("/api/tutorials"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should create a new tutorial")
    void itShouldCreateTutorial() throws Exception {
        mockMvc.perform(post("/api/tutorials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new Tutorial("Tut title", "Tut desc", true))))
                .andExpect(status().isCreated());

        assertThat(tutorialRepository.findAll()).hasSize(1);
    }

    @Test
    @DisplayName("Should return all tutorials from db")
    void itShouldReturnAListOfTutorials() throws Exception {
        Tutorial tut1 = new Tutorial("Tut1 title", "Tut1 desc", true);
        tutorialRepository.save(tut1);

        Tutorial tut2 = new Tutorial("Tut2 title", "Tut2 desc", false);
        tutorialRepository.save(tut2);

        Tutorial tut3 = new Tutorial("Tut3 title", "Tut3 desc", true);
        tutorialRepository.save(tut3);

        mockMvc.perform(get("/api/tutorials"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    @DisplayName("Should return tutorial by id")
    void itShouldReturnTutorialById() throws Exception {
        Tutorial tut1 = new Tutorial("Tut1 title", "Tut1 desc", true);
        tutorialRepository.save(tut1);

        Tutorial tut2 = new Tutorial("Tut2 title", "Tut2 desc", false);
        tutorialRepository.save(tut2);

        Tutorial tut3 = new Tutorial("Tut3 title", "Tut3 desc", true);
        tutorialRepository.save(tut3);

        mockMvc.perform(get("/api/tutorials/" + tut2.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(tut2.getId()))
                .andExpect(jsonPath("$.title").value(tut2.getTitle()))
                .andExpect(jsonPath("$.description").value(tut2.getDescription()))
                .andExpect(jsonPath("$.published").value(tut2.isPublished()));
    }

    @Test
    @DisplayName("Should return not found if given id does not exist")
    void itShouldReturnNotFoundIfGivenIdDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/tutorials/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return all tutorials with given title")
    void itShouldReturnAllTutorialsWithGivenTitle() throws Exception {
        Tutorial tut1 = new Tutorial("Tut1", "Tut1 desc", true);
        tutorialRepository.save(tut1);

        Tutorial tut2 = new Tutorial("Tut2 title", "Tut2 desc", false);
        tutorialRepository.save(tut2);

        Tutorial tut3 = new Tutorial("Tut3 title", "Tut3 desc", true);
        tutorialRepository.save(tut3);

        mockMvc.perform(get("/api/tutorials?title=title"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("should return not found if given id does not exist when trying to update")
    void itShouldReturnNotFoundIfGivenIdDoesNotExistWhenTryingToUpdate() throws Exception {
        mockMvc.perform(put("/api/tutorials/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new Tutorial("Tut title", "Tut desc", true))))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should update tutorial")
    void itShouldUpdateTutorial() throws Exception {
        Tutorial tut1 = new Tutorial("Tut1 title", "Tut1 desc", true);
        tutorialRepository.save(tut1);

        Tutorial tut2 = new Tutorial("Tut2 title", "Tut2 desc", false);
        tutorialRepository.save(tut2);

        Tutorial tut3 = new Tutorial("Tut3 title", "Tut3 desc", true);
        tutorialRepository.save(tut3);

        final String updatedTitle = "Tut2 title updated";
        final String updatedDescription = "Tut2 desc updated";

        mockMvc.perform(put("/api/tutorials/" + tut2.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new Tutorial(updatedTitle, updatedDescription, true))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(tut2.getId()))
                .andExpect(jsonPath("$.title").value(updatedTitle))
                .andExpect(jsonPath("$.description").value(updatedDescription))
                .andExpect(jsonPath("$.published").value(true));
    }

    @Test
    @DisplayName("Should delete tutorial by id")
    void itShouldDeleteTutorialById() throws Exception {
        Tutorial tut1 = new Tutorial("Tut1 title", "Tut1 desc", true);
        tutorialRepository.save(tut1);

        Tutorial tut2 = new Tutorial("Tut2 title", "Tut2 desc", false);
        tutorialRepository.save(tut2);

        Tutorial tut3 = new Tutorial("Tut3 title", "Tut3 desc", true);
        tutorialRepository.save(tut3);

        mockMvc.perform(delete("/api/tutorials/" + tut2.getId()))
                .andExpect(status().isNoContent());

        assertThat(tutorialRepository.findAll()).hasSize(2).contains(tut1, tut3);
    }

    @Test
    @DisplayName("Should delete all tutorials")
    void itShouldDeleteAllTutorials() throws Exception {
        Tutorial tut1 = new Tutorial("Tut1 title", "Tut1 desc", true);
        tutorialRepository.save(tut1);

        Tutorial tut2 = new Tutorial("Tut2 title", "Tut2 desc", false);
        tutorialRepository.save(tut2);

        Tutorial tut3 = new Tutorial("Tut3 title", "Tut3 desc", true);
        tutorialRepository.save(tut3);

        mockMvc.perform(delete("/api/tutorials"))
                .andExpect(status().isNoContent());

        assertThat(tutorialRepository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("Should return all published tutorials")
    void itShouldReturnAllPublishedTutorials() throws Exception {
        Tutorial tut1 = new Tutorial("Tut1 title", "Tut1 desc", true);
        tutorialRepository.save(tut1);

        Tutorial tut2 = new Tutorial("Tut2 title", "Tut2 desc", false);
        tutorialRepository.save(tut2);

        Tutorial tut3 = new Tutorial("Tut3 title", "Tut3 desc", true);
        tutorialRepository.save(tut3);

        mockMvc.perform(get("/api/tutorials/published"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("Should return no content if no published tutorials")
    void itShouldReturnNoContentIfNoPublishedTutorials() throws Exception {
        mockMvc.perform(get("/api/tutorials/published"))
                .andExpect(status().isNoContent());
    }

}
