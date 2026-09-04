package com.inventoryapi.service;

import com.inventoryapi.dto.CategorieRequestDTO;
import com.inventoryapi.dto.CategorieResponseDTO;
import com.inventoryapi.entity.Categorie;
import com.inventoryapi.exception.CategorieNotFoundException;
import com.inventoryapi.repository.CategorieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CategorieServiceImpl implements CategorieService {

    private final CategorieRepository categorieRepository;
    private final InventoryMapper mapper;

    @Override
    public CategorieResponseDTO createCategorie(CategorieRequestDTO dto) {
        if (categorieRepository.existsByNomIgnoreCase(dto.getNom())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Une catégorie avec le nom '" + dto.getNom() + "' existe déjà.");
        }
        Categorie categorie = Categorie.builder()
                .nom(dto.getNom())
                .description(dto.getDescription())
                .build();
        return mapper.toCategorieDTO(categorieRepository.save(categorie));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategorieResponseDTO> getAllCategories() {
        return categorieRepository.findAll().stream()
                .map(mapper::toCategorieDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CategorieResponseDTO getCategorieById(Long id) {
        return mapper.toCategorieDTO(findOrThrow(id));
    }

    @Override
    public CategorieResponseDTO updateCategorie(Long id, CategorieRequestDTO dto) {
        Categorie categorie = findOrThrow(id);
        categorie.setNom(dto.getNom());
        categorie.setDescription(dto.getDescription());
        return mapper.toCategorieDTO(categorieRepository.save(categorie));
    }

    @Override
    public void deleteCategorie(Long id) {
        Categorie categorie = findOrThrow(id);
        if (!categorie.getProduits().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Impossible de supprimer la catégorie '" + categorie.getNom() +
                    "' : elle contient " + categorie.getProduits().size() + " produit(s).");
        }
        categorieRepository.deleteById(id);
    }

    private Categorie findOrThrow(Long id) {
        return categorieRepository.findById(id)
                .orElseThrow(() -> new CategorieNotFoundException(id));
    }
}
