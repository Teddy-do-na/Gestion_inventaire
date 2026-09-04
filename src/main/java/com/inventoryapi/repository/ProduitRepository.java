package com.inventoryapi.repository;

import com.inventoryapi.entity.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProduitRepository extends JpaRepository<Produit, Long> {

    Optional<Produit> findByReference(String reference);

    boolean existsByReference(String reference);

    boolean existsByReferenceAndIdNot(String reference, Long id);

    List<Produit> findByCategorieId(Long categorieId);

    @Query("SELECT p FROM Produit p WHERE p.quantiteEnStock <= :seuil AND p.quantiteEnStock > 0")
    List<Produit> findByStockBas(@Param("seuil") int seuil);

    @Query("SELECT p FROM Produit p WHERE p.quantiteEnStock = 0")
    List<Produit> findByStockEpuise();

    @Query("SELECT p FROM Produit p WHERE p.quantiteEnStock <= :seuil")
    List<Produit> findByStockBasOuEpuise(@Param("seuil") int seuil);

    @Query("SELECT p FROM Produit p WHERE LOWER(p.nom) LIKE LOWER(CONCAT('%', :mot, '%')) " +
           "OR LOWER(p.reference) LIKE LOWER(CONCAT('%', :mot, '%'))")
    List<Produit> searchByMotCle(@Param("mot") String motCle);

    @Query("SELECT COUNT(p) FROM Produit p WHERE p.quantiteEnStock = 0")
    long countByStockEpuise();

    @Query("SELECT COUNT(p) FROM Produit p WHERE p.quantiteEnStock <= :seuil AND p.quantiteEnStock > 0")
    long countByStockBas(@Param("seuil") int seuil);
}
