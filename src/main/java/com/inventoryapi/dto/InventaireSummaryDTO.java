package com.inventoryapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@Schema(description = "Résumé global de l'inventaire")
public class InventaireSummaryDTO {

    @Schema(description = "Nombre total de produits référencés")
    private long totalProduits;

    @Schema(description = "Nombre de produits en stock normal")
    private long produitsEnStock;

    @Schema(description = "Nombre de produits en stock bas (sous le seuil)")
    private long produitsStockBas;

    @Schema(description = "Nombre de produits épuisés (stock = 0)")
    private long produitsEpuises;

    @Schema(description = "Valeur totale de l'inventaire")
    private BigDecimal valeurTotaleInventaire;

    @Schema(description = "Nombre total de catégories")
    private long totalCategories;

    @Schema(description = "Seuil d'alerte actuel")
    private int seuilAlerte;
}
