package com.inventoryapi.service;

import com.inventoryapi.dto.InventaireSummaryDTO;
import com.inventoryapi.dto.MouvementStockRequestDTO;
import com.inventoryapi.dto.MouvementStockResponseDTO;
import com.inventoryapi.dto.ProduitResponseDTO;
import com.inventoryapi.dto.StockAlertDTO;
import com.inventoryapi.entity.MouvementStock;
import com.inventoryapi.entity.Produit;
import com.inventoryapi.enums.TypeMouvement;
import com.inventoryapi.exception.ProduitNotFoundException;
import com.inventoryapi.exception.StockInsuffisantException;
import com.inventoryapi.repository.CategorieRepository;
import com.inventoryapi.repository.MouvementStockRepository;
import com.inventoryapi.repository.ProduitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class StockServiceImpl implements StockService {

    private final ProduitRepository produitRepository;
    private final MouvementStockRepository mouvementRepository;
    private final CategorieRepository categorieRepository;
    private final InventoryMapper mapper;

    @Value("${inventory.stock.seuil-alerte}")
    private int seuilAlerte;

    @Override
    public MouvementStockResponseDTO enregistrerMouvement(Long produitId, MouvementStockRequestDTO dto) {
        Produit produit = produitRepository.findById(produitId)
                .orElseThrow(() -> new ProduitNotFoundException(produitId));

        int stockAvant = produit.getQuantiteEnStock();
        int nouveauStock = calculerNouveauStock(produit, dto);

        produit.setQuantiteEnStock(nouveauStock);
        produitRepository.save(produit);

        MouvementStock mouvement = MouvementStock.builder()
                .type(dto.getType())
                .quantite(dto.getQuantite())
                .stockAvant(stockAvant)
                .stockApres(nouveauStock)
                .motif(dto.getMotif())
                .produit(produit)
                .build();

        return mapper.toMouvementDTO(mouvementRepository.save(mouvement));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MouvementStockResponseDTO> getHistoriqueMouvements(Long produitId) {
        if (!produitRepository.existsById(produitId)) {
            throw new ProduitNotFoundException(produitId);
        }
        return mouvementRepository.findByProduitIdOrderByDateOperationDesc(produitId).stream()
                .map(mapper::toMouvementDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public StockAlertDTO getAlertes() {
        List<Produit> produitsEnAlerte = produitRepository.findByStockBasOuEpuise(seuilAlerte);

        List<ProduitResponseDTO> dtos = produitsEnAlerte.stream()
                .map(mapper::toProduitDTO)
                .collect(Collectors.toList());

        long epuises = produitsEnAlerte.stream().filter(p -> p.getQuantiteEnStock() == 0).count();
        long stockBas = produitsEnAlerte.stream().filter(p -> p.getQuantiteEnStock() > 0).count();

        BigDecimal valeurTotale = produitsEnAlerte.stream()
                .map(p -> p.getPrix().multiply(BigDecimal.valueOf(p.getQuantiteEnStock())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return StockAlertDTO.builder()
                .totalProduitsStockBas((int) stockBas)
                .totalProduitsEpuises((int) epuises)
                .seuilAlerte(seuilAlerte)
                .valeurTotaleEnAlerte(valeurTotale)
                .produits(dtos)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public InventaireSummaryDTO getSummary() {
        long total = produitRepository.count();
        long epuises = produitRepository.countByStockEpuise();
        long stockBas = produitRepository.countByStockBas(seuilAlerte);
        long enStock = total - epuises - stockBas;

        BigDecimal valeurTotale = produitRepository.findAll().stream()
                .map(p -> p.getPrix().multiply(BigDecimal.valueOf(p.getQuantiteEnStock())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return InventaireSummaryDTO.builder()
                .totalProduits(total)
                .produitsEnStock(enStock)
                .produitsStockBas(stockBas)
                .produitsEpuises(epuises)
                .valeurTotaleInventaire(valeurTotale)
                .totalCategories(categorieRepository.count())
                .seuilAlerte(seuilAlerte)
                .build();
    }

    private int calculerNouveauStock(Produit produit, MouvementStockRequestDTO dto) {
        return switch (dto.getType()) {
            case ENTREE -> produit.getQuantiteEnStock() + dto.getQuantite();
            case SORTIE -> {
                if (produit.getQuantiteEnStock() < dto.getQuantite()) {
                    throw new StockInsuffisantException(
                            produit.getNom(), produit.getQuantiteEnStock(), dto.getQuantite());
                }
                yield produit.getQuantiteEnStock() - dto.getQuantite();
            }
            case AJUSTEMENT -> dto.getQuantite();
        };
    }
}
