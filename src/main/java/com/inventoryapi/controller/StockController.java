package com.inventoryapi.controller;

import com.inventoryapi.dto.ApiResponse;
import com.inventoryapi.dto.InventaireSummaryDTO;
import com.inventoryapi.dto.StockAlertDTO;
import com.inventoryapi.service.StockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
@Tag(name = "Stock & Alertes", description = "Surveillance du stock, alertes et tableau de bord")
@CrossOrigin(origins = "*")
public class StockController {

    private final StockService stockService;

    @GetMapping("/alertes")
    @Operation(
        summary = "Obtenir les alertes de stock bas",
        description = "Retourne tous les produits dont le stock est épuisé (0) ou en dessous du seuil configuré (défaut : 5 unités)."
    )
    public ResponseEntity<ApiResponse<StockAlertDTO>> getAlertes() {
        StockAlertDTO alertes = stockService.getAlertes();
        String message = alertes.getTotalProduitsEpuises() + " épuisé(s), " +
                alertes.getTotalProduitsStockBas() + " en stock bas (seuil : " +
                alertes.getSeuilAlerte() + ")";
        return ResponseEntity.ok(ApiResponse.success(alertes, message));
    }

    @GetMapping("/summary")
    @Operation(
        summary = "Tableau de bord de l'inventaire",
        description = "Vue globale : nombre de produits, valeur totale, catégories, alertes."
    )
    public ResponseEntity<ApiResponse<InventaireSummaryDTO>> getSummary() {
        return ResponseEntity.ok(
                ApiResponse.success(stockService.getSummary(), "Résumé de l'inventaire"));
    }
}
