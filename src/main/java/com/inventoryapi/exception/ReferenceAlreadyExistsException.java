package com.inventoryapi.exception;

public class ReferenceAlreadyExistsException extends RuntimeException {
    public ReferenceAlreadyExistsException(String reference) {
        super("Un produit avec la référence '" + reference + "' existe déjà.");
    }
}
