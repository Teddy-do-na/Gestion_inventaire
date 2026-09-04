package com.inventoryapi.dto;

import com.inventoryapi.enums.TypeMouvement;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Données pour enregistrer un mouvement de stock")
public class MouvementStockRequestDTO {

    @NotNull(message = "Le type de mouvement est obligatoire")
    @Schema(description = "Type de mouvement : ENTREE, SORTIE ou AJUSTEMENT", example = "ENTREE")
    private TypeMouvement type;

    @NotNull(message = "La quantité est obligatoire")
    @Min(value = 1, message = "La quantité doit être supérieure à 0")
    @Schema(description = "Quantité du mouvement", example = "10")
    private Integer quantite;

    @Schema(description = "Motif du mouvement", example = "Réapprovisionnement fournisseur")
    private String motif;
}
