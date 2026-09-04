package com.inventoryapi.dto;

import com.inventoryapi.enums.TypeMouvement;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@Schema(description = "Mouvement de stock enregistré")
public class MouvementStockResponseDTO {

    @Schema(description = "ID du mouvement", example = "1")
    private Long id;

    @Schema(description = "Type : ENTREE, SORTIE, AJUSTEMENT")
    private TypeMouvement type;

    @Schema(description = "Quantité déplacée", example = "10")
    private Integer quantite;

    @Schema(description = "Stock avant l'opération", example = "40")
    private Integer stockAvant;

    @Schema(description = "Stock après l'opération", example = "50")
    private Integer stockApres;

    @Schema(description = "Motif de l'opération")
    private String motif;

    @Schema(description = "Date et heure de l'opération")
    private LocalDateTime dateOperation;

    @Schema(description = "ID du produit concerné", example = "1")
    private Long produitId;

    @Schema(description = "Nom du produit concerné", example = "Laptop Dell XPS 15")
    private String produitNom;
}
