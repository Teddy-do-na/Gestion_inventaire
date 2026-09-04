package com.inventoryapi.controller;

import com.inventoryapi.dto.ApiResponse;
import com.inventoryapi.dto.MouvementStockRequestDTO;
import com.inventoryapi.dto.MouvementStockResponseDTO;
import com.inventoryapi.dto.ProduitRequestDTO;
import com.inventoryapi.dto.ProduitResponseDTO;
import com.inventoryapi.service.ProduitService;
import com.inventoryapi.service.StockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produits")
@RequiredArgsConstructor
@Tag(name = "Produits", description = "Gestion des produits de l'inventaire")
@CrossOrigin(origins = "*")
public class ProduitController {

    private final ProduitService produitService;
    private final StockService stockService;

    @PostMapping
    @Operation(
        summary = "Créer un nouveau produit",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(examples = {
                @ExampleObject(name = "Laptop", value = """
                    {
                      "nom": "Laptop Dell XPS 15",
                      "description": "Ordinateur portable haute performance",
                      "prix": 1299.99,
                      "quantiteEnStock": 20,
                      "reference": "DELL-XPS-15-2024",
                      "categorieId": 1
                    }"""),
                @ExampleObject(name = "Souris", value = """
                    {
                      "nom": "Souris Logitech MX Master 3",
                      "description": "Souris sans fil ergonomique",
                      "prix": 89.99,
                      "quantiteEnStock": 3,
                      "reference": "LOG-MX3-2024",
                      "categorieId": 1
                    }""")
            })
        )
    )
    public ResponseEntity<ApiResponse<ProduitResponseDTO>> createProduit(
            @Valid @RequestBody ProduitRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(produitService.createProduit(dto), "Produit créé avec succès"));
    }

    @GetMapping
    @Operation(summary = "Lister tous les produits")
    public ResponseEntity<ApiResponse<List<ProduitResponseDTO>>> getAllProduits() {
        return ResponseEntity.ok(ApiResponse.success(produitService.getAllProduits(), "Liste des produits"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un produit par son ID")
    public ResponseEntity<ApiResponse<ProduitResponseDTO>> getProduitById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(produitService.getProduitById(id), "Produit trouvé"));
    }

    @GetMapping("/reference/{reference}")
    @Operation(summary = "Récupérer un produit par sa référence")
    public ResponseEntity<ApiResponse<ProduitResponseDTO>> getProduitByReference(
            @Parameter(description = "Référence du produit", example = "DELL-XPS-15-2024")
            @PathVariable String reference) {
        return ResponseEntity.ok(
                ApiResponse.success(produitService.getProduitByReference(reference), "Produit trouvé"));
    }

    @GetMapping("/categorie/{categorieId}")
    @Operation(summary = "Lister les produits d'une catégorie")
    public ResponseEntity<ApiResponse<List<ProduitResponseDTO>>> getProduitsByCategorie(
            @PathVariable Long categorieId) {
        return ResponseEntity.ok(
                ApiResponse.success(produitService.getProduitsByCategorie(categorieId),
                        "Produits de la catégorie " + categorieId));
    }

    @GetMapping("/search")
    @Operation(summary = "Rechercher des produits par nom ou référence")
    public ResponseEntity<ApiResponse<List<ProduitResponseDTO>>> searchProduits(
            @Parameter(description = "Mot-clé de recherche", example = "Dell")
            @RequestParam String q) {
        return ResponseEntity.ok(
                ApiResponse.success(produitService.searchProduits(q), "Résultats pour : " + q));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un produit")
    public ResponseEntity<ApiResponse<ProduitResponseDTO>> updateProduit(
            @PathVariable Long id, @Valid @RequestBody ProduitRequestDTO dto) {
        return ResponseEntity.ok(
                ApiResponse.success(produitService.updateProduit(id, dto), "Produit mis à jour"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un produit")
    public ResponseEntity<ApiResponse<Void>> deleteProduit(@PathVariable Long id) {
        produitService.deleteProduit(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Produit supprimé avec succès"));
    }

    @PostMapping("/{id}/stock/mouvement")
    @Operation(
        summary = "Enregistrer un mouvement de stock",
        description = "**ENTREE** : augmente le stock | **SORTIE** : diminue le stock | **AJUSTEMENT** : fixe le stock à la valeur donnée",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(examples = {
                @ExampleObject(name = "Entrée stock", value = """
                    {"type": "ENTREE", "quantite": 10, "motif": "Réapprovisionnement fournisseur"}"""),
                @ExampleObject(name = "Sortie stock", value = """
                    {"type": "SORTIE", "quantite": 5, "motif": "Vente client #1042"}"""),
                @ExampleObject(name = "Ajustement inventaire", value = """
                    {"type": "AJUSTEMENT", "quantite": 15, "motif": "Correction après inventaire physique"}""")
            })
        )
    )
    public ResponseEntity<ApiResponse<MouvementStockResponseDTO>> enregistrerMouvement(
            @PathVariable Long id,
            @Valid @RequestBody MouvementStockRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        stockService.enregistrerMouvement(id, dto), "Mouvement enregistré"));
    }

    @GetMapping("/{id}/stock/historique")
    @Operation(summary = "Consulter l'historique des mouvements de stock d'un produit")
    public ResponseEntity<ApiResponse<List<MouvementStockResponseDTO>>> getHistorique(
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(
                stockService.getHistoriqueMouvements(id), "Historique des mouvements"));
    }
}
