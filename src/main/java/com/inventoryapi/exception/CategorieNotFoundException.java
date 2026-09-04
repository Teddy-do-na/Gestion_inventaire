package com.inventoryapi.exception;

public class CategorieNotFoundException extends RuntimeException {
    public CategorieNotFoundException(Long id) {
        super("Catégorie introuvable avec l'id : " + id);
    }
}
