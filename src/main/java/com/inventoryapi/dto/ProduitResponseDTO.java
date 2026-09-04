package com.inventoryapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@Schema(description = "Données d'un produit retourné par l'API")
public class ProduitResponseDTO {

    @Schema(description = "Identifiant unique", example = "1")
    private Long id;

    @Schema(description = "Nom du produit", example = "Laptop Dell XPS 15")
    private String nom;

    @Schema(description = "Description du produit")
    private String description;

    @Schema(description = "Prix unitaire", example = "1299.99")
    private BigDecimal prix;

    @Schema(description = "Quantité en stock", example = "50")
    private Integer quantiteEnStock;

    @Schema(description = "Référence unique", example = "DELL-XPS-15-2024")
    private String reference;

    @Schema(description = "Catégorie du produit")
    private String categorie;

    @Schema(description = "Vrai si le stock est bas (sous le seuil d'alerte)")
    private boolean stockBas;

    @Schema(description = "Vrai si le stock est épuisé")
    private boolean stockEpuise;

    @Schema(description = "Valeur totale du stock (prix × quantité)", example = "64999.50")
    private BigDecimal valeurStock;

    @Schema(description = "Date de création")
    private LocalDateTime createdAt;

    @Schema(description = "Date de dernière mise à jour")
    private LocalDateTime updatedAt;
}
