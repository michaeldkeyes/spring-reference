package com.zoola.tutorial.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@Data
@Entity
@NoArgsConstructor
@Schema(description = "Tutorial model")
@Table(name = "tutorials")
public class Tutorial {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Tutorial ID", example = "123")
    private Long id;

    @Column(name = "title")
    @Schema(description = "Tutorial title", example = "Swagger Tutorial")
    private String title;

    @Column(name = "description")
    @Schema(description = "Tutorial description", example = "Learn how to use Swagger")
    private String description;

    @Column(name = "published")
    @Schema(description = "Whether or not a Tutorial is published", example = "true")
    private boolean published;

    public Tutorial(String title, String description, boolean published) {
        this.title = title;
        this.description = description;
        this.published = published;
    }
}
