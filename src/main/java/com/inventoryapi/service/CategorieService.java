package com.inventoryapi.service;

import com.inventoryapi.dto.CategorieRequestDTO;
import com.inventoryapi.dto.CategorieResponseDTO;

import java.util.List;

public interface CategorieService {
    CategorieResponseDTO createCategorie(CategorieRequestDTO dto);
    List<CategorieResponseDTO> getAllCategories();
    CategorieResponseDTO getCategorieById(Long id);
    CategorieResponseDTO updateCategorie(Long id, CategorieRequestDTO dto);
    void deleteCategorie(Long id);
}
