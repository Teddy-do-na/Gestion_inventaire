package com.inventoryapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Inventory API")
                        .version("1.0.0")
                        .description("""
                                API REST de gestion d'inventaire de produits avec suivi des stocks.
                                
                                **Fonctionnalités :**
                                - CRUD complet sur les produits et catégories
                                - Mouvements de stock : ENTREE, SORTIE, AJUSTEMENT
                                - Historique complet des mouvements par produit
                                - Alertes automatiques : stock bas (< seuil) et épuisé (= 0)
                                - Tableau de bord global de l'inventaire
                                - Recherche par nom ou référence
                                
                                **Seuil d'alerte :** configurable dans `application.properties` via `inventory.stock.seuil-alerte` (défaut : 5)
                                """)
                        .contact(new Contact().name("Dev Team").email("contact@inventoryapi.com")));
    }
}
