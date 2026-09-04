package com.inventoryapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@Schema(description = "Rapport d'alertes de stock")
public class StockAlertDTO {

    @Schema(description = "Nombre total de produits en stock bas")
    private int totalProduitsStockBas;

    @Schema(description = "Nombre total de produits épuisés")
    private int totalProduitsEpuises;

    @Schema(description = "Seuil d'alerte configuré", example = "5")
    private int seuilAlerte;

    @Schema(description = "Valeur totale des stocks en alerte")
    private BigDecimal valeurTotaleEnAlerte;

    @Schema(description = "Liste des produits en stock bas ou épuisés")
    private List<ProduitResponseDTO> produits;
}
