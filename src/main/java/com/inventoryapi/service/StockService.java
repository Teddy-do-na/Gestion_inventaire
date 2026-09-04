package com.inventoryapi.service;

import com.inventoryapi.dto.InventaireSummaryDTO;
import com.inventoryapi.dto.MouvementStockRequestDTO;
import com.inventoryapi.dto.MouvementStockResponseDTO;
import com.inventoryapi.dto.StockAlertDTO;

import java.util.List;

public interface StockService {
    MouvementStockResponseDTO enregistrerMouvement(Long produitId, MouvementStockRequestDTO dto);
    List<MouvementStockResponseDTO> getHistoriqueMouvements(Long produitId);
    StockAlertDTO getAlertes();
    InventaireSummaryDTO getSummary();
}
