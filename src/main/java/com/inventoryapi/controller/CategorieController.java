package com.inventoryapi.controller;

import com.inventoryapi.dto.ApiResponse;
import com.inventoryapi.dto.CategorieRequestDTO;
import com.inventoryapi.dto.CategorieResponseDTO;
import com.inventoryapi.service.CategorieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "Catégories", description = "Gestion des catégories de produits")
@CrossOrigin(origins = "*")
public class CategorieController {

    private final CategorieService categorieService;

    @PostMapping
    @Operation(summary = "Créer une catégorie")
    public ResponseEntity<ApiResponse<CategorieResponseDTO>> createCategorie(
            @Valid @RequestBody CategorieRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(categorieService.createCategorie(dto), "Catégorie créée"));
    }

    @GetMapping
    @Operation(summary = "Lister toutes les catégories")
    public ResponseEntity<ApiResponse<List<CategorieResponseDTO>>> getAllCategories() {
        return ResponseEntity.ok(
                ApiResponse.success(categorieService.getAllCategories(), "Liste des catégories"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une catégorie par son ID")
    public ResponseEntity<ApiResponse<CategorieResponseDTO>> getCategorieById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success(categorieService.getCategorieById(id), "Catégorie trouvée"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour une catégorie")
    public ResponseEntity<ApiResponse<CategorieResponseDTO>> updateCategorie(
            @PathVariable Long id, @Valid @RequestBody CategorieRequestDTO dto) {
        return ResponseEntity.ok(
                ApiResponse.success(categorieService.updateCategorie(id, dto), "Catégorie mise à jour"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une catégorie (impossible si elle contient des produits)")
    public ResponseEntity<ApiResponse<Void>> deleteCategorie(@PathVariable Long id) {
        categorieService.deleteCategorie(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Catégorie supprimée avec succès"));
    }
}
