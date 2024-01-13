package com.zoola.tutorial.controller;

import com.zoola.tutorial.model.Tutorial;
import com.zoola.tutorial.service.TutorialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TutorialController {

    private final TutorialService tutorialService;

    @GetMapping("/tutorials")
    public ResponseEntity<List<Tutorial>> getAllTutorials(@RequestParam(required = false) String title) {
        try {
            List<Tutorial> tutorials = tutorialService.getAllTutorials(title);

            if (tutorials.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(tutorials);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/tutorials/{id}")
    public ResponseEntity<Tutorial> getTutorialById(@PathVariable("id") long id) {
        Tutorial tutorial = tutorialService.getTutorialById(id);

        if (tutorial == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(tutorial);
    }

    @PostMapping("/tutorials")
    public ResponseEntity<Tutorial> createTutorial(@RequestBody Tutorial tutorial) {
        try {
            Tutorial _tutorial = tutorialService.createTutorial(tutorial);

            return ResponseEntity.ok(_tutorial);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/tutorials/{id}")
    public ResponseEntity<Tutorial> updateTutorial(@PathVariable("id") long id, @RequestBody Tutorial tutorial) {
        Tutorial _tutorial = tutorialService.updateTutorial(tutorial);

        if (_tutorial == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(tutorialService.createTutorial(_tutorial));
    }

    @DeleteMapping("/tutorials/{id}")
    public ResponseEntity<HttpStatus> deleteTutorial(@PathVariable("id") long id) {
        try {
            tutorialService.deleteById(id);

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/tutorials")
    public ResponseEntity<HttpStatus> deleteAllTutorials() {
        try {
            tutorialService.deleteAll();

            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/tutorials/published")
    public ResponseEntity<List<Tutorial>> findByPublished() {
        try {
            List<Tutorial> tutorials = tutorialService.findByPublished();

            if (tutorials.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(tutorials);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
