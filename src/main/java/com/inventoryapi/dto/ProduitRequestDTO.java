package com.inventoryapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Données pour créer ou mettre à jour un produit")
public class ProduitRequestDTO {

    @NotBlank(message = "Le nom est obligatoire")
    @Size(min = 2, max = 150, message = "Le nom doit contenir entre 2 et 150 caractères")
    @Schema(description = "Nom du produit", example = "Laptop Dell XPS 15")
    private String nom;

    @Schema(description = "Description du produit", example = "Ordinateur portable haute performance")
    private String description;

    @NotNull(message = "Le prix est obligatoire")
    @DecimalMin(value = "0.01", message = "Le prix doit être supérieur à 0")
    @Digits(integer = 8, fraction = 2, message = "Format prix invalide (max 8 entiers, 2 décimales)")
    @Schema(description = "Prix unitaire", example = "1299.99")
    private BigDecimal prix;

    @NotNull(message = "La quantité en stock est obligatoire")
    @Min(value = 0, message = "La quantité ne peut pas être négative")
    @Schema(description = "Quantité initiale en stock", example = "50")
    private Integer quantiteEnStock;

    @NotBlank(message = "La référence est obligatoire")
    @Schema(description = "Référence unique du produit", example = "DELL-XPS-15-2024")
    private String reference;

    @Schema(description = "ID de la catégorie", example = "1")
    private Long categorieId;
}
