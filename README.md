# Inventory API — Spring Boot

API REST de gestion d'inventaire de produits avec suivi des stocks.

## Stack Technologique

- **Java 17**
- **Spring Boot 3.2.5**
- **Spring Data JPA** + PostgreSQL
- **Lombok**
- **Springdoc OpenAPI** (Swagger UI)

## Architecture

```
src/main/java/com/inventoryapi/
├── controller/     # ProduitController, StockController, CategorieController
├── service/        # ProduitService, StockService, CategorieService (interfaces + impls), InventoryMapper
├── repository/     # ProduitRepository, CategorieRepository, MouvementStockRepository
├── entity/         # Produit, Categorie, MouvementStock
├── dto/            # ProduitRequestDTO, ProduitResponseDTO, MouvementStockRequestDTO,
│                     MouvementStockResponseDTO, CategorieRequestDTO, CategorieResponseDTO,
│                     StockAlertDTO, InventaireSummaryDTO, ApiResponse
├── enums/          # TypeMouvement (ENTREE, SORTIE, AJUSTEMENT)
├── exception/      # ProduitNotFoundException, CategorieNotFoundException,
│                     StockInsuffisantException, ReferenceAlreadyExistsException,
│                     GlobalExceptionHandler
└── config/         # SwaggerConfig, DataInitializer (données de démo)
```

---

## Prérequis

- Java 17+
- Maven 3.8+
- PostgreSQL 14+ (serveur démarré)

---

## Configuration de la base de données

### 1. Créer la base dans PostgreSQL

```sql
CREATE DATABASE inventorydb;
```

### 2. Configurer les accès

Éditer `src/main/resources/application.properties` :

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/inventorydb
spring.datasource.username=postgres
spring.datasource.password=postgres
```

> Remplacer `postgres` / `postgres` par votre nom d'utilisateur et mot de passe PostgreSQL.

---

## Lancer le projet

```bash
# Cloner le dépôt
git clone <url-du-repo>
cd inventory-api

# Lancer l'application
mvn spring-boot:run
```

L'API démarre sur **`http://localhost:8085`**

Au démarrage, des **données de démonstration** sont automatiquement insérées :
- 3 catégories (Informatique, Bureautique, Réseau)
- 6 produits dont 2 en stock bas et 1 épuisé
- 7 mouvements de stock historiques

---

## URLs importantes

| URL | Description |
|-----|-------------|
| `http://localhost:8085/swagger-ui.html` | Documentation Swagger interactive |
| `http://localhost:8085/api-docs` | Schéma OpenAPI JSON |

---

## Endpoints

### Produits

| Méthode | URL | Description |
|---------|-----|-------------|
| POST | /api/produits | Créer un produit |
| GET | /api/produits | Lister tous les produits |
| GET | /api/produits/{id} | Récupérer un produit par ID |
| GET | /api/produits/reference/{ref} | Récupérer par référence |
| GET | /api/produits/categorie/{id} | Produits d'une catégorie |
| GET | /api/produits/search?q= | Rechercher par nom ou référence |
| PUT | /api/produits/{id} | Mettre à jour un produit |
| DELETE | /api/produits/{id} | Supprimer un produit |

### Mouvements de stock

| Méthode | URL | Description |
|---------|-----|-------------|
| POST | /api/produits/{id}/stock/mouvement | Enregistrer un mouvement |
| GET | /api/produits/{id}/stock/historique | Historique des mouvements |

### Stock & Alertes

| Méthode | URL | Description |
|---------|-----|-------------|
| GET | /api/stock/alertes | Produits en stock bas ou épuisés |
| GET | /api/stock/summary | Tableau de bord global |

### Catégories

| Méthode | URL | Description |
|---------|-----|-------------|
| POST | /api/categories | Créer une catégorie |
| GET | /api/categories | Lister toutes les catégories |
| GET | /api/categories/{id} | Récupérer une catégorie |
| PUT | /api/categories/{id} | Mettre à jour |
| DELETE | /api/categories/{id} | Supprimer (impossible si elle a des produits) |

---

## Exemples de requêtes

### Créer un produit

```bash
curl -X POST http://localhost:8085/api/produits \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Laptop Dell XPS 15",
    "description": "Ordinateur portable haute performance",
    "prix": 1299.99,
    "quantiteEnStock": 20,
    "reference": "DELL-XPS-15-2024",
    "categorieId": 1
  }'
```

### Enregistrer un mouvement de stock

```bash
# Entrée stock
curl -X POST http://localhost:8085/api/produits/1/stock/mouvement \
  -H "Content-Type: application/json" \
  -d '{"type": "ENTREE", "quantite": 10, "motif": "Réapprovisionnement fournisseur"}'

# Sortie stock
curl -X POST http://localhost:8085/api/produits/1/stock/mouvement \
  -H "Content-Type: application/json" \
  -d '{"type": "SORTIE", "quantite": 5, "motif": "Vente client #1042"}'

# Ajustement (fixe le stock à la valeur donnée)
curl -X POST http://localhost:8085/api/produits/1/stock/mouvement \
  -H "Content-Type: application/json" \
  -d '{"type": "AJUSTEMENT", "quantite": 15, "motif": "Correction après inventaire physique"}'
```

### Consulter les alertes de stock

```bash
curl http://localhost:8085/api/stock/alertes
```

**Réponse :**
```json
{
  "success": true,
  "message": "1 épuisé(s), 2 en stock bas (seuil : 5)",
  "data": {
    "totalProduitsStockBas": 2,
    "totalProduitsEpuises": 1,
    "seuilAlerte": 5,
    "valeurTotaleEnAlerte": 329.95,
    "produits": [
      {
        "id": 3,
        "nom": "Souris Logitech MX Master 3",
        "quantiteEnStock": 3,
        "stockBas": true,
        "stockEpuise": false
      },
      {
        "id": 5,
        "nom": "Clavier mécanique Keychron K2",
        "quantiteEnStock": 0,
        "stockBas": false,
        "stockEpuise": true
      }
    ]
  }
}
```

### Tableau de bord

```bash
curl http://localhost:8085/api/stock/summary
```

---

## Types de mouvements de stock

| Type | Effet |
|------|-------|
| `ENTREE` | Augmente le stock du produit |
| `SORTIE` | Diminue le stock (erreur si stock insuffisant) |
| `AJUSTEMENT` | Fixe le stock à la valeur exacte donnée |

---

## Gestion des erreurs

| Cas | Code HTTP | Message |
|-----|-----------|---------|
| Produit introuvable | 404 | "Produit introuvable avec l'id : X" |
| Référence déjà existante | 409 | "Un produit avec la référence '...' existe déjà." |
| Stock insuffisant (SORTIE) | 409 | "Stock insuffisant pour '...'." |
| Catégorie non vide (DELETE) | 409 | "Impossible de supprimer la catégorie '...' : elle contient N produit(s)." |
| Validation échouée | 400 | Détail des champs en erreur |

---

## Configurer le seuil d'alerte

Le seuil est configurable dans `application.properties` :

```properties
inventory.stock.seuil-alerte=5
```

Modifier cette valeur et redémarrer l'application pour changer le seuil.

---

## Notes pour le Frontend (Angular / React)

- CORS activé sur tous les endpoints (`@CrossOrigin(origins = "*")`)
- Format uniforme : `{ success, message, data }`
- Les champs `stockBas` et `stockEpuise` sont calculés automatiquement dans chaque réponse produit
- Base URL : `http://localhost:8085/api`

## Lancer les tests

```bash
mvn test
```
