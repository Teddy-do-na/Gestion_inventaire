package com.inventoryapi.service;

import com.inventoryapi.dto.CategorieResponseDTO;
import com.inventoryapi.dto.MouvementStockResponseDTO;
import com.inventoryapi.dto.ProduitResponseDTO;
import com.inventoryapi.entity.Categorie;
import com.inventoryapi.entity.MouvementStock;
import com.inventoryapi.entity.Produit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class InventoryMapper {

    @Value("${inventory.stock.seuil-alerte}")
    private int seuilAlerte;

    public ProduitResponseDTO toProduitDTO(Produit p) {
        boolean stockBas = p.getQuantiteEnStock() > 0 && p.getQuantiteEnStock() <= seuilAlerte;
        boolean stockEpuise = p.getQuantiteEnStock() == 0;
        BigDecimal valeurStock = p.getPrix().multiply(BigDecimal.valueOf(p.getQuantiteEnStock()));

        return ProduitResponseDTO.builder()
                .id(p.getId())
                .nom(p.getNom())
                .description(p.getDescription())
                .prix(p.getPrix())
                .quantiteEnStock(p.getQuantiteEnStock())
                .reference(p.getReference())
                .categorie(p.getCategorie() != null ? p.getCategorie().getNom() : null)
                .stockBas(stockBas)
                .stockEpuise(stockEpuise)
                .valeurStock(valeurStock)
                .createdAt(p.getCreatedAt())
                .updatedAt(p.getUpdatedAt())
                .build();
    }

    public MouvementStockResponseDTO toMouvementDTO(MouvementStock m) {
        return MouvementStockResponseDTO.builder()
                .id(m.getId())
                .type(m.getType())
                .quantite(m.getQuantite())
                .stockAvant(m.getStockAvant())
                .stockApres(m.getStockApres())
                .motif(m.getMotif())
                .dateOperation(m.getDateOperation())
                .produitId(m.getProduit().getId())
                .produitNom(m.getProduit().getNom())
                .build();
    }

    public CategorieResponseDTO toCategorieDTO(Categorie c) {
        return CategorieResponseDTO.builder()
                .id(c.getId())
                .nom(c.getNom())
                .description(c.getDescription())
                .nombreProduits(c.getProduits().size())
                .createdAt(c.getCreatedAt())
                .build();
    }
}
