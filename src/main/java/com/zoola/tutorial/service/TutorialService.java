package com.zoola.tutorial.service;

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

    public List<Tutorial> getAllTutorials(String title) {
        if (title == null) {
            return tutorialRepository.findAll();
        } else {
            return tutorialRepository.findByTitleContaining(title);
        }
    }

    public Tutorial getTutorialById(long id) {
        return tutorialRepository.findById(id).orElse(null);
    }

    public Tutorial createTutorial(Tutorial tutorial) {
        return tutorialRepository.save(tutorial);
    }

    public Tutorial updateTutorial(Tutorial tutorial) {
        Tutorial _tutorial = tutorialRepository.findById(tutorial.getId()).orElse(null);

        if (_tutorial == null) {
            return null;
        }

        _tutorial.setTitle(tutorial.getTitle());
        _tutorial.setDescription(tutorial.getDescription());
        _tutorial.setPublished(tutorial.isPublished());

        return tutorialRepository.save(_tutorial);
    }

    public void deleteById(long id) {
        tutorialRepository.deleteById(id);
    }

    public void deleteAll() {
        tutorialRepository.deleteAll();
    }

    public List<Tutorial> findByPublished() {
        return tutorialRepository.findByPublished(true);
    }
}
