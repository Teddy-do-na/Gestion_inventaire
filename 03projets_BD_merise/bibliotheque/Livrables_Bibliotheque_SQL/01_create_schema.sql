-- 01_create_schema.sql
-- Schéma de la base pour le projet Bibliothèque Universitaire
-- Compatible MySQL 8+
DROP DATABASE IF EXISTS bibliotheque;
CREATE DATABASE bibliotheque CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE bibliotheque;

-- Table Genres
CREATE TABLE Genres (
  id_genre INT AUTO_INCREMENT PRIMARY KEY,
  nom_genre VARCHAR(100) NOT NULL,
  UNIQUE KEY uk_genres_nom (nom_genre)
) ENGINE=InnoDB;

-- Table Etudiants
CREATE TABLE Etudiants (
  id_etudiant INT AUTO_INCREMENT PRIMARY KEY,
  nom VARCHAR(100) NOT NULL,
  prenom VARCHAR(100) NOT NULL,
  date_naissance DATE NOT NULL,
  email VARCHAR(150) NOT NULL,
  telephone VARCHAR(30),
  date_inscription DATE NOT NULL DEFAULT (CURRENT_DATE),
  UNIQUE KEY uk_etudiants_email (email)
) ENGINE=InnoDB;

-- Table Livres
-- NB: 'exemplaires_disponibles' est ajouté pour répondre à l'exigence de mise à jour via triggers
CREATE TABLE Livres (
  id_livre INT AUTO_INCREMENT PRIMARY KEY,
  titre VARCHAR(255) NOT NULL,
  auteur VARCHAR(150) NOT NULL,
  annee_publication YEAR NOT NULL,
  id_genre INT NOT NULL,
  nombre_exemplaires INT NOT NULL CHECK (nombre_exemplaires >= 0),
  exemplaires_disponibles INT NOT NULL CHECK (exemplaires_disponibles >= 0),
  CONSTRAINT fk_livres_genre FOREIGN KEY (id_genre) REFERENCES Genres(id_genre)
    ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

-- Table Emprunts
CREATE TABLE Emprunts (
  id_emprunt INT AUTO_INCREMENT PRIMARY KEY,
  id_etudiant INT NOT NULL,
  id_livre INT NOT NULL,
  date_emprunt DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  date_retour DATETIME NULL,
  CONSTRAINT fk_emprunts_etudiant FOREIGN KEY (id_etudiant) REFERENCES Etudiants(id_etudiant)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT fk_emprunts_livre FOREIGN KEY (id_livre) REFERENCES Livres(id_livre)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT chk_dates_retour CHECK (date_retour IS NULL OR date_retour >= date_emprunt)
) ENGINE=InnoDB;