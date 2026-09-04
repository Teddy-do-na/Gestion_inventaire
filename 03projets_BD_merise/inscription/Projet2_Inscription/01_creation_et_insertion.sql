

CREATE DATABASE IF NOT EXISTS inscriptionskfokam48;

-- On se positionne sur la base de données créée
USE inscriptionskfokam48;
-- TABLE: formations

CREATE TABLE IF NOT EXISTS formations (
id_formation INT PRIMARY KEY AUTO_INCREMENT,
nom_formation VARCHAR(160) NOT NULL,
description TEXT,
date_debut DATE NOT NULL,
date_fin DATE NOT NULL,
tarif DECIMAL(12,2) NOT NULL CHECK (tarif >= 0),
nombre_places INTEGER NOT NULL CHECK (nombre_places >= 0)
);


-- TABLE: etudiants

CREATE TABLE IF NOT EXISTS etudiants (
id_etudiant INT PRIMARY KEY AUTO_INCREMENT,
nom VARCHAR(100) NOT NULL,
prenom VARCHAR(100) NOT NULL,
date_naissance DATE NOT NULL,
email VARCHAR(150) NOT NULL,
telephone VARCHAR(30) NOT NULL,
date_inscription DATE NOT NULL,
CONSTRAINT uq_etudiants_email UNIQUE (email)
);


-- TABLE: inscriptions
-- - FK explicites vers etudiants et formations
-- - UNIQUE(id_etudiant, id_formation) pour éviter les doublons
-- - statut_paiement contrôlé par ENUM

CREATE TABLE IF NOT EXISTS inscriptions (
id_inscription INT PRIMARY KEY AUTO_INCREMENT,
id_etudiant INT NOT NULL,
id_formation INT NOT NULL,
date_inscription DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
statut_paiement ENUM('En attente', 'Payé') NOT NULL DEFAULT 'En attente',
CONSTRAINT ak_inscriptions_etud_form UNIQUE (id_etudiant, id_formation),
CONSTRAINT fk_inscriptions_etudiant
FOREIGN KEY (id_etudiant)
REFERENCES etudiants(id_etudiant)
ON UPDATE CASCADE ON DELETE RESTRICT,
CONSTRAINT fk_inscriptions_formation
FOREIGN KEY (id_formation)
REFERENCES formations(id_formation)
ON UPDATE CASCADE ON DELETE RESTRICT
);


CREATE INDEX idx_inscriptions_formation ON inscriptions(id_formation);
CREATE INDEX idx_inscriptions_etudiant ON inscriptions(id_etudiant);


-- TABLE: paiements
-- - FK explicite vers inscriptions

CREATE TABLE IF NOT EXISTS paiements (
id_paiement INT PRIMARY KEY AUTO_INCREMENT,
id_inscription INT NOT NULL,
montant DECIMAL(12,2) NOT NULL CHECK (montant > 0),
date_paiement TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
methode_paiement VARCHAR(40) NOT NULL,
CONSTRAINT fk_paiements_inscription
FOREIGN KEY (id_inscription)
REFERENCES inscriptions(id_inscription)
ON UPDATE CASCADE ON DELETE RESTRICT
);
-- TABLE: paiements_archive (historisation)
-- - Pas de FK pour ne pas bloquer l’archivage si l’origine est supprimée

CREATE TABLE IF NOT EXISTS paiements_archive (
id_archive INT PRIMARY KEY AUTO_INCREMENT,
id_paiement INT NOT NULL,
id_inscription INT NOT NULL,
montant DECIMAL(12,2) NOT NULL,
date_paiement TIMESTAMP NOT NULL,
methode_paiement VARCHAR(40) NOT NULL,
archived_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);


-- Formations
INSERT INTO formations (nom_formation, description, date_debut, date_fin, tarif, nombre_places) VALUES
('DevOps Essentials', 'Introduction aux pipelines CI/CD et conteneurs', '2025-10-01', '2025-10-05', 150000, 30),
('Data Science 101', 'Fondamentaux de la data science', '2025-10-10', '2025-10-14', 180000, 25),
('Web Full-Stack', 'Front-end/Back-end modernes', '2025-11-01', '2025-11-08', 200000, 40)
ON DUPLICATE KEY UPDATE nom_formation = nom_formation;

-- Étudiants
INSERT INTO etudiants (nom, prenom, date_naissance, email, telephone, date_inscription) VALUES
('Jordan','Nandjo','2001-04-12','jordannandjo@yahoo.com','(+237)690000001','2025-09-01'),
('Ngono','Estelle','2000-07-09','estelle.ngono@univ.example','(+237)690000002','2025-09-02'),
('Ndongo','Marc','1999-02-21','marc.ndongo@univ.example','(+237)690000003','2025-09-03')
ON DUPLICATE KEY UPDATE email = email;

-- Inscriptions (exemples)
INSERT INTO inscriptions (id_etudiant, id_formation, date_inscription)
SELECT e.id_etudiant, f.id_formation, CURDATE()
FROM etudiants e JOIN formations f ON f.nom_formation='DevOps Essentials'
WHERE e.email='jordannandjo@yahoo.com'
ON DUPLICATE KEY UPDATE  inscriptions.id_etudiant =  inscriptions.id_etudiant;

INSERT INTO inscriptions (id_etudiant, id_formation, date_inscription)
SELECT e.id_etudiant, f.id_formation, CURDATE()
FROM etudiants e JOIN formations f ON f.nom_formation='Web Full-Stack'
WHERE e.email='estelle.ngono@univ.example'
ON DUPLICATE KEY UPDATE  inscriptions.id_etudiant =  inscriptions.id_etudiant;

-- Paiement (exemple : 1 paiement pour Jordan)
INSERT INTO paiements (id_inscription, montant, methode_paiement)
SELECT i.id_inscription, 150000, 'Mobile Money'
FROM inscriptions i
JOIN etudiants e ON e.id_etudiant = i.id_etudiant
JOIN formations f ON f.id_formation = i.id_formation
WHERE e.email='jordannandjo@yahoo.com' AND f.nom_formation='DevOps Essentials';

