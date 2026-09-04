package com.inventoryapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Données pour créer une catégorie")
public class CategorieRequestDTO {

    @NotBlank(message = "Le nom de la catégorie est obligatoire")
    @Schema(description = "Nom de la catégorie", example = "Informatique")
    private String nom;

    @Schema(description = "Description de la catégorie", example = "Ordinateurs, périphériques et accessoires")
    private String description;
}
