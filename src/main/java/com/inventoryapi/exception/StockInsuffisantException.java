package com.inventoryapi.exception;

public class StockInsuffisantException extends RuntimeException {
    public StockInsuffisantException(String nomProduit, int stockActuel, int quantiteDemandee) {
        super(String.format(
            "Stock insuffisant pour '%s'. Stock actuel : %d, quantité demandée : %d.",
            nomProduit, stockActuel, quantiteDemandee
        ));
    }
}
