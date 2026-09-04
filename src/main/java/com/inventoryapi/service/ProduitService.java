package com.inventoryapi.service;

import com.inventoryapi.dto.ProduitRequestDTO;
import com.inventoryapi.dto.ProduitResponseDTO;

import java.util.List;

public interface ProduitService {
    ProduitResponseDTO createProduit(ProduitRequestDTO dto);
    List<ProduitResponseDTO> getAllProduits();
    ProduitResponseDTO getProduitById(Long id);
    ProduitResponseDTO getProduitByReference(String reference);
    List<ProduitResponseDTO> getProduitsByCategorie(Long categorieId);
    List<ProduitResponseDTO> searchProduits(String motCle);
    ProduitResponseDTO updateProduit(Long id, ProduitRequestDTO dto);
    void deleteProduit(Long id);
}
