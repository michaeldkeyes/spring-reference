package com.zoola.tutorial.service;

import com.zoola.tutorial.model.Tutorial;
import com.zoola.tutorial.repository.TutorialRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TutorialServiceTests {

    @Mock
    private TutorialRepository tutorialRepository;

    @InjectMocks
    private TutorialService tutorialService;

    @Test
    @DisplayName("Should return all tutorials from the repository")
    public void shouldReturnAllTutorialsFromRepository() {
        final List<Tutorial> tutorials = new ArrayList<>(
                List.of(
                        new Tutorial(1L, "title1", "description1", true),
                        new Tutorial(2L, "title2", "description2", false),
                        new Tutorial(3L, "title3", "description3", true)
                )
        );

        when(tutorialRepository.findAll()).thenReturn(tutorials);

        final List<Tutorial> actual = tutorialService.getAllTutorials(null);

        verify(tutorialRepository, times(1)).findAll();
        assertThat(actual).hasSize(3);
    }

    @Test
    @DisplayName("Should return all tutorials from the repository with a title containing the given string")
    public void shouldReturnAllTutorialsFromRepositoryWithTitleContaining() {
        final String title = "title";
        when(tutorialRepository.findByTitleContaining(title))
                .thenReturn(List.of(new Tutorial("Tut title 1", "Tut desc 1", true)));

        final List<Tutorial> tutorials = tutorialService.getAllTutorials(title);

        verify(tutorialRepository, times(1)).findByTitleContaining(title);
        assertThat(tutorials).hasSize(1);
    }

    @Test
    @DisplayName("Should return a tutorial from the repository with the given id")
    public void shouldReturnATutorialFromRepositoryWithGivenId() {
        final long id = 1L;
        final Tutorial tutorial = new Tutorial(id, "Tut title 1", "Tut desc 1", true);
        when(tutorialRepository.findById(id)).thenReturn(Optional.of(tutorial));

        final Tutorial actual = tutorialService.getTutorialById(id);

        verify(tutorialRepository, times(1)).findById(id);
        assertThat(actual).isEqualTo(tutorial);
    }

    @Test
    @DisplayName("Should return null when a tutorial with the given id does not exist")
    public void shouldReturnNullWhenATutorialWithGivenIdDoesNotExist() {
        final long id = 1L;
        when(tutorialRepository.findById(id)).thenReturn(Optional.empty());

        final Tutorial actual = tutorialService.getTutorialById(id);

        verify(tutorialRepository, times(1)).findById(id);
        assertThat(actual).isNull();
    }

    @Test
    @DisplayName("Should create a tutorial in the repository")
    public void shouldCreateATutorialInTheRepository() {
        final Tutorial tutorial = new Tutorial("Tut title 1", "Tut desc 1", true);
        when(tutorialRepository.save(tutorial)).thenReturn(tutorial);

        final Tutorial actual = tutorialService.createTutorial(tutorial);

        verify(tutorialRepository, times(1)).save(tutorial);
        assertThat(actual).isEqualTo(tutorial);
    }

    @Test
    @DisplayName("Should update a tutorial in the repository")
    public void shouldUpdateATutorialInTheRepository() {
        final Tutorial tutorial = new Tutorial(1L, "Tut title 1", "Tut desc 1", true);
        final Tutorial tutorialInDb = new Tutorial(1L, "Tut title 1 updated", "Tut desc 1 updated", true);

        when(tutorialRepository.findById(tutorial.getId())).thenReturn(Optional.of(tutorialInDb));
        when(tutorialRepository.save(tutorial)).thenReturn(tutorialInDb);

        final Tutorial actual = tutorialService.updateTutorial(tutorial);

        verify(tutorialRepository, times(1)).findById(tutorial.getId());
        verify(tutorialRepository, times(1)).save(tutorialInDb);
        assertThat(actual).isEqualTo(tutorialInDb);
    }

    @Test
    @DisplayName("Should return null when a tutorial with the given id does not exist when updating")
    public void shouldReturnNullWhenATutorialWithGivenIdDoesNotExistWhenUpdating() {
        final Tutorial tutorial = new Tutorial(1L, "Tut title 1", "Tut desc 1", true);
        when(tutorialRepository.findById(tutorial.getId())).thenReturn(Optional.empty());

        final Tutorial actual = tutorialService.updateTutorial(tutorial);

        verify(tutorialRepository, times(1)).findById(tutorial.getId());
        assertThat(actual).isNull();
    }

    @Test
    @DisplayName("Should delete a tutorial from the repository with the given id")
    public void shouldDeleteATutorialFromRepositoryWithGivenId() {
        final long id = 1L;
        doNothing().when(tutorialRepository).deleteById(id);

        tutorialService.deleteById(id);

        verify(tutorialRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Should delete all tutorials from the repository")
    public void shouldDeleteAllTutorialsFromRepository() {
        doNothing().when(tutorialRepository).deleteAll();

        tutorialService.deleteAll();

        verify(tutorialRepository, times(1)).deleteAll();
    }

    @Test
    @DisplayName("Should return all published tutorials from the repository")
    public void shouldReturnAllPublishedTutorialsFromRepository() {
        final List<Tutorial> tutorials = new ArrayList<>(
                List.of(
                        new Tutorial(1L, "title1", "description1", true),
                        new Tutorial(2L, "title2", "description2", false),
                        new Tutorial(3L, "title3", "description3", true)
                )
        );

        when(tutorialRepository.findByPublished(true)).thenReturn(List.of(tutorials.get(0), tutorials.get(2)));

        final List<Tutorial> actual = tutorialService.findByPublished();

        verify(tutorialRepository, times(1)).findByPublished(true);
        assertThat(actual).hasSize(3);
    }
}
