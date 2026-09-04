package com.inventoryapi.service;

import com.inventoryapi.dto.ProduitRequestDTO;
import com.inventoryapi.dto.ProduitResponseDTO;
import com.inventoryapi.entity.Categorie;
import com.inventoryapi.entity.Produit;
import com.inventoryapi.exception.CategorieNotFoundException;
import com.inventoryapi.exception.ProduitNotFoundException;
import com.inventoryapi.exception.ReferenceAlreadyExistsException;
import com.inventoryapi.repository.CategorieRepository;
import com.inventoryapi.repository.ProduitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProduitServiceImpl implements ProduitService {

    private final ProduitRepository produitRepository;
    private final CategorieRepository categorieRepository;
    private final InventoryMapper mapper;

    @Override
    public ProduitResponseDTO createProduit(ProduitRequestDTO dto) {
        if (produitRepository.existsByReference(dto.getReference())) {
            throw new ReferenceAlreadyExistsException(dto.getReference());
        }

        Categorie categorie = resolveCategorie(dto.getCategorieId());

        Produit produit = Produit.builder()
                .nom(dto.getNom())
                .description(dto.getDescription())
                .prix(dto.getPrix())
                .quantiteEnStock(dto.getQuantiteEnStock())
                .reference(dto.getReference())
                .categorie(categorie)
                .build();

        return mapper.toProduitDTO(produitRepository.save(produit));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProduitResponseDTO> getAllProduits() {
        return produitRepository.findAll().stream()
                .map(mapper::toProduitDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProduitResponseDTO getProduitById(Long id) {
        return mapper.toProduitDTO(findOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public ProduitResponseDTO getProduitByReference(String reference) {
        return mapper.toProduitDTO(
                produitRepository.findByReference(reference)
                        .orElseThrow(() -> new ProduitNotFoundException(reference)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProduitResponseDTO> getProduitsByCategorie(Long categorieId) {
        if (!categorieRepository.existsById(categorieId)) {
            throw new CategorieNotFoundException(categorieId);
        }
        return produitRepository.findByCategorieId(categorieId).stream()
                .map(mapper::toProduitDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProduitResponseDTO> searchProduits(String motCle) {
        return produitRepository.searchByMotCle(motCle).stream()
                .map(mapper::toProduitDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProduitResponseDTO updateProduit(Long id, ProduitRequestDTO dto) {
        Produit produit = findOrThrow(id);

        if (produitRepository.existsByReferenceAndIdNot(dto.getReference(), id)) {
            throw new ReferenceAlreadyExistsException(dto.getReference());
        }

        Categorie categorie = resolveCategorie(dto.getCategorieId());

        produit.setNom(dto.getNom());
        produit.setDescription(dto.getDescription());
        produit.setPrix(dto.getPrix());
        produit.setQuantiteEnStock(dto.getQuantiteEnStock());
        produit.setReference(dto.getReference());
        produit.setCategorie(categorie);

        return mapper.toProduitDTO(produitRepository.save(produit));
    }

    @Override
    public void deleteProduit(Long id) {
        findOrThrow(id);
        produitRepository.deleteById(id);
    }

    private Produit findOrThrow(Long id) {
        return produitRepository.findById(id)
                .orElseThrow(() -> new ProduitNotFoundException(id));
    }

    private Categorie resolveCategorie(Long categorieId) {
        if (categorieId == null) return null;
        return categorieRepository.findById(categorieId)
                .orElseThrow(() -> new CategorieNotFoundException(categorieId));
    }
}
