package com.inventoryapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@Schema(description = "Catégorie retournée par l'API")
public class CategorieResponseDTO {

    @Schema(description = "ID de la catégorie", example = "1")
    private Long id;

    @Schema(description = "Nom de la catégorie", example = "Informatique")
    private String nom;

    @Schema(description = "Description", example = "Ordinateurs, périphériques et accessoires")
    private String description;

    @Schema(description = "Nombre de produits dans cette catégorie", example = "12")
    private int nombreProduits;

    @Schema(description = "Date de création")
    private LocalDateTime createdAt;
}
