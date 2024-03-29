package com.zoola.tutorial.service;

import com.zoola.tutorial.exception.ResourceNotFoundException;
import com.zoola.tutorial.model.Tutorial;
import com.zoola.tutorial.repository.TutorialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TutorialService {

    private final TutorialRepository tutorialRepository;

    public List<Tutorial> getAllTutorials(final String title) {
        if (title == null) {
            return tutorialRepository.findAll();
        } else {
            return tutorialRepository.findByTitleContaining(title);
        }
    }

    public Tutorial getTutorialById(final long id) {
        return tutorialRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tutorial not found with id: " + id));
    }

    public Tutorial createTutorial(final Tutorial tutorial) {
        return tutorialRepository.save(tutorial);
    }

    public Tutorial updateTutorial(final long id, final Tutorial tutorial) {
        Tutorial _tutorial = tutorialRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tutorial not found with id: " + id));

        _tutorial.setTitle(tutorial.getTitle());
        _tutorial.setDescription(tutorial.getDescription());
        _tutorial.setPublished(tutorial.isPublished());

        return tutorialRepository.save(_tutorial);
    }

    public void deleteById(final long id) {
        tutorialRepository.deleteById(id);
    }

    public void deleteAll() {
        tutorialRepository.deleteAll();
    }

    public List<Tutorial> findByPublished() {
        return tutorialRepository.findByPublished(true);
    }
}
