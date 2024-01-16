package com.zoola.tutorial.repository;

import com.zoola.tutorial.model.Tutorial;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class TutorialRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TutorialRepository tutorialRepository;

    @Test
    @DisplayName("Should find no tutorials if repository is empty")
    public void should_find_no_tutorials_if_repository_is_empty() {
        Iterable<Tutorial> tutorials = tutorialRepository.findAll();

        assertThat(tutorials).isEmpty();
    }

    @Test
    @DisplayName("Should store a tutorial")
    public void should_store_a_tutorial() {
        Tutorial tutorial = tutorialRepository.save(new Tutorial("Tut title", "Tut desc", true));

        assertThat(tutorial).hasFieldOrPropertyWithValue("title", "Tut title");
        assertThat(tutorial).hasFieldOrPropertyWithValue("description", "Tut desc");
        assertThat(tutorial).hasFieldOrPropertyWithValue("published", true);
    }

    @Test
    @DisplayName("Should find all tutorials")
    public void should_find_all_tutorials() {
        Tutorial tut1 = new Tutorial("Tut1 title", "Tut1 desc", true);
        entityManager.persist(tut1);

        Tutorial tut2 = new Tutorial("Tut2 title", "Tut2 desc", false);
        entityManager.persist(tut2);

        Tutorial tut3 = new Tutorial("Tut3 title", "Tut3 desc", true);
        entityManager.persist(tut3);

        Iterable<Tutorial> tutorials = tutorialRepository.findAll();

        assertThat(tutorials).hasSize(3).contains(tut1, tut2, tut3);
    }

    @Test
    @DisplayName("Should find tutorial by id")
    public void should_find_tutorial_by_id() {
        Tutorial tut1 = new Tutorial("Tut1 title", "Tut1 desc", true);
        entityManager.persist(tut1);

        Tutorial tut2 = new Tutorial("Tut2 title", "Tut2 desc", false);
        entityManager.persist(tut2);

        Tutorial foundTutorial = tutorialRepository.findById(tut2.getId()).get();

        assertThat(foundTutorial).isEqualTo(tut2);
    }

    @Test
    @DisplayName("Should find published tutorials")
    public void should_find_published_tutorials() {
        Tutorial tut1 = new Tutorial("Tut1 title", "Tut1 desc", true);
        entityManager.persist(tut1);

        Tutorial tut2 = new Tutorial("Tut2 title", "Tut2 desc", false);
        entityManager.persist(tut2);

        Tutorial tut3 = new Tutorial("Tut3 title", "Tut3 desc", true);
        entityManager.persist(tut3);

        Iterable<Tutorial> tutorials = tutorialRepository.findByPublished(true);

        assertThat(tutorials).hasSize(2).contains(tut1, tut3);
    }

    @Test
    @DisplayName("Should find tutorial by title containing")
    public void should_find_tutorial_by_title_containing() {
        Tutorial tut1 = new Tutorial("Spring Boot Tut1", "Tut1 desc", true);
        entityManager.persist(tut1);

        Tutorial tut2 = new Tutorial("Java Tut2", "Tut2 desc", false);
        entityManager.persist(tut2);

        Tutorial tut3 = new Tutorial("Spring Data JPA Tut3", "Tut3 desc", true);
        entityManager.persist(tut3);

        Iterable<Tutorial> tutorials = tutorialRepository.findByTitleContaining("ring");

        assertThat(tutorials).hasSize(2).contains(tut1, tut3);
    }

    @Test
    @DisplayName("Should update tutorial by id")
    public void should_update_tutorial_by_id() {
        Tutorial tut1 = new Tutorial("Tut1 title", "Tut1 desc", true);
        entityManager.persist(tut1);

        Tutorial tut2 = new Tutorial("Tut2 title", "Tut2 desc", false);
        entityManager.persist(tut2);

        Tutorial updatedTut = new Tutorial("updated Tut2 title", "updated Tut2 desc", true);

        Tutorial tut = tutorialRepository.findById(tut2.getId()).get();
        tut.setTitle(updatedTut.getTitle());
        tut.setDescription(updatedTut.getDescription());
        tut.setPublished(updatedTut.isPublished());
        tutorialRepository.save(tut);

        Tutorial checkTut = tutorialRepository.findById(tut2.getId()).get();

        assertThat(checkTut.getId()).isEqualTo(tut2.getId());
        assertThat(checkTut.getTitle()).isEqualTo(updatedTut.getTitle());
        assertThat(checkTut.getDescription()).isEqualTo(updatedTut.getDescription());
        assertThat(checkTut.isPublished()).isEqualTo(updatedTut.isPublished());
    }

    @Test
    @DisplayName("Should delete tutorial by id")
    public void should_delete_tutorial_by_id() {
        Tutorial tut1 = new Tutorial("Tut1 title", "Tut1 desc", true);
        entityManager.persist(tut1);

        Tutorial tut2 = new Tutorial("Tut2 title", "Tut2 desc", false);
        entityManager.persist(tut2);

        Tutorial tut3 = new Tutorial("Tut3 title", "Tut3 desc", true);
        entityManager.persist(tut3);

        tutorialRepository.deleteById(tut2.getId());

        Iterable<Tutorial> tutorials = tutorialRepository.findAll();

        assertThat(tutorials).hasSize(2).contains(tut1, tut3);
    }

    @Test
    @DisplayName("Should delete all tutorials")
    public void should_delete_all_tutorials() {
        entityManager.persist(new Tutorial("Tut1 title", "Tut1 desc", true));
        entityManager.persist(new Tutorial("Tut2 title", "Tut2 desc", false));

        tutorialRepository.deleteAll();

        assertThat(tutorialRepository.findAll()).isEmpty();
    }
}
