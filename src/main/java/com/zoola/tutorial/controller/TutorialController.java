package com.zoola.tutorial.controller;

import com.zoola.tutorial.model.Tutorial;
import com.zoola.tutorial.service.TutorialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Tutorial", description = "Tutorial API")
public class TutorialController {

    private final TutorialService tutorialService;

    @Operation(
            summary = "Get all tutorials",
            description = "Get all tutorials from database",
            tags = {"tutorials", "get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Tutorial.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "204", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})
    })
    @Parameters({
            @Parameter(name = "title", description = "Tutorial title", in = ParameterIn.QUERY, schema = @Schema(implementation = String.class))
    })
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

            return new ResponseEntity<>(_tutorial, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/tutorials/{id}")
    public ResponseEntity<Tutorial> updateTutorial(@PathVariable("id") long id, @RequestBody Tutorial tutorial) {
        Tutorial _tutorial = tutorialService.updateTutorial(tutorial);

        System.out.println("Update Tutorial with ID = " + id);

        if (_tutorial == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(_tutorial);
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
