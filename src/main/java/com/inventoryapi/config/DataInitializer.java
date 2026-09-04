package com.inventoryapi.config;

import com.inventoryapi.entity.Categorie;
import com.inventoryapi.entity.MouvementStock;
import com.inventoryapi.entity.Produit;
import com.inventoryapi.enums.TypeMouvement;
import com.inventoryapi.repository.CategorieRepository;
import com.inventoryapi.repository.MouvementStockRepository;
import com.inventoryapi.repository.ProduitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CategorieRepository categorieRepository;
    private final ProduitRepository produitRepository;
    private final MouvementStockRepository mouvementRepository;

    @Override
    public void run(String... args) {
        Categorie informatique = categorieRepository.save(
                Categorie.builder().nom("Informatique").description("Ordinateurs, périphériques et accessoires").build());
        Categorie bureautique = categorieRepository.save(
                Categorie.builder().nom("Bureautique").description("Fournitures et matériel de bureau").build());
        Categorie reseau = categorieRepository.save(
                Categorie.builder().nom("Réseau").description("Équipements réseau et connectique").build());

        // Produits stock normal
        Produit laptop = produitRepository.save(Produit.builder()
                .nom("Laptop Dell XPS 15").description("Ordinateur portable haute performance")
                .prix(new BigDecimal("1299.99")).quantiteEnStock(20)
                .reference("DELL-XPS-15-2024").categorie(informatique).build());

        Produit ecran = produitRepository.save(Produit.builder()
                .nom("Écran LG 27 pouces 4K").description("Moniteur 4K UHD IPS")
                .prix(new BigDecimal("449.99")).quantiteEnStock(15)
                .reference("LG-27UK850-2024").categorie(informatique).build());

        // Produit stock bas (seuil = 5)
        Produit souris = produitRepository.save(Produit.builder()
                .nom("Souris Logitech MX Master 3").description("Souris sans fil ergonomique")
                .prix(new BigDecimal("89.99")).quantiteEnStock(3)
                .reference("LOG-MX3-2024").categorie(informatique).build());

        Produit cle = produitRepository.save(Produit.builder()
                .nom("Clé USB SanDisk 64Go").description("Clé USB 3.0 haute vitesse")
                .prix(new BigDecimal("14.99")).quantiteEnStock(4)
                .reference("SAN-USB64-2024").categorie(bureautique).build());

        // Produit épuisé
        Produit clavier = produitRepository.save(Produit.builder()
                .nom("Clavier mécanique Keychron K2").description("Clavier mécanique compact sans fil")
                .prix(new BigDecimal("109.99")).quantiteEnStock(0)
                .reference("KEY-K2-2024").categorie(informatique).build());

        Produit switch8 = produitRepository.save(Produit.builder()
                .nom("Switch TP-Link 8 ports").description("Switch réseau Gigabit 8 ports")
                .prix(new BigDecimal("34.99")).quantiteEnStock(10)
                .reference("TPL-SW8-2024").categorie(reseau).build());

        // Historique mouvements
        saveMouvement(laptop, TypeMouvement.ENTREE, 20, 0, 20, "Stock initial");
        saveMouvement(laptop, TypeMouvement.SORTIE, 5, 20, 15, "Vente lot entreprise");
        saveMouvement(laptop, TypeMouvement.ENTREE, 5, 15, 20, "Réapprovisionnement");
        saveMouvement(souris, TypeMouvement.ENTREE, 10, 0, 10, "Stock initial");
        saveMouvement(souris, TypeMouvement.SORTIE, 7, 10, 3, "Ventes diverses");
        saveMouvement(clavier, TypeMouvement.ENTREE, 5, 0, 5, "Stock initial");
        saveMouvement(clavier, TypeMouvement.SORTIE, 5, 5, 0, "Rupture de stock");

        System.out.println("\n✅ Données de démonstration chargées :");
        System.out.println("   - 3 catégories, 6 produits");
        System.out.println("   - 2 produits en stock bas (souris: 3, clé USB: 4)");
        System.out.println("   - 1 produit épuisé (clavier)");
        System.out.println("   - 7 mouvements de stock enregistrés\n");
    }

    private void saveMouvement(Produit produit, TypeMouvement type,
                                int quantite, int avant, int apres, String motif) {
        mouvementRepository.save(MouvementStock.builder()
                .produit(produit).type(type).quantite(quantite)
                .stockAvant(avant).stockApres(apres).motif(motif).build());
    }
}
