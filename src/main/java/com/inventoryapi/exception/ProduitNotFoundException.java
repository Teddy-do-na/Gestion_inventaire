package com.inventoryapi.exception;

public class ProduitNotFoundException extends RuntimeException {
    public ProduitNotFoundException(Long id) {
        super("Produit introuvable avec l'id : " + id);
    }
    public ProduitNotFoundException(String reference) {
        super("Produit introuvable avec la référence : " + reference);
    }
}
