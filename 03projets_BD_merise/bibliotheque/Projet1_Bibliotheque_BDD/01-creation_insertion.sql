-- phpMyAdmin SQL Dump
-- version 5.2.2
-- https://www.phpmyadmin.net/
--
-- Hôte : db
-- Généré le : mar. 09 sep. 2025 à 09:47
-- Version du serveur : 8.0.43
-- Version de PHP : 8.2.27

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `bibliotheque`
--

-- --------------------------------------------------------

--
-- Structure de la table `emprunts`
--

CREATE TABLE `emprunts` (
  `id_emprunt` bigint NOT NULL,
  `id_etudiant` int NOT NULL,
  `id_livre` bigint NOT NULL,
  `date_emprunt` date NOT NULL,
  `date_retour` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `emprunts`
--

INSERT INTO `emprunts` (`id_emprunt`, `id_etudiant`, `id_livre`, `date_emprunt`, `date_retour`) VALUES
(1, 1, 1, '2024-06-01', NULL);

-- --------------------------------------------------------

--
-- Structure de la table `etudiants`
--

CREATE TABLE `etudiants` (
  `id_etudiant` int NOT NULL,
  `nom` varchar(100) NOT NULL,
  `prenom` varchar(100) NOT NULL,
  `date_naissance` date NOT NULL,
  `email` varchar(150) NOT NULL,
  `telephone` varchar(30) NOT NULL,
  `date_inscription` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `etudiants`
--

INSERT INTO `etudiants` (`id_etudiant`, `nom`, `prenom`, `date_naissance`, `email`, `telephone`, `date_inscription`) VALUES
(1, 'Mc', 'Jordan', '2001-04-12', 'jordanmc@gmail.com', '+237690000001', '2023-10-01'),
(2, 'Ngono', 'Estelle', '2000-07-09', 'estelle.ngono@etu.univ.example', '+237690000002', '2022-11-15'),
(3, 'Ndongo', 'Marc', '1999-02-21', 'marc.ndongo@etu.univ.example', '+237690000003', '2024-01-10');

-- --------------------------------------------------------

--
-- Structure de la table `genres`
--

CREATE TABLE `genres` (
  `id_genre` int NOT NULL,
  `nom_genre` varchar(80) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `genres`
--

INSERT INTO `genres` (`id_genre`, `nom_genre`) VALUES
(4, 'Histoire'),
(1, 'Informatique'),
(3, 'Littérature'),
(2, 'Mathématiques'),
(5, 'Sciences');

-- --------------------------------------------------------

--
-- Structure de la table `livres`
--

CREATE TABLE `livres` (
  `id_livre` bigint NOT NULL,
  `titre` varchar(255) NOT NULL,
  `auteur` varchar(180) NOT NULL,
  `annee_publication` smallint NOT NULL,
  `id_genre` int NOT NULL,
  `nombre_exemplaires` int NOT NULL
) ;

--
-- Déchargement des données de la table `livres`
--

INSERT INTO `livres` (`id_livre`, `titre`, `auteur`, `annee_publication`, `id_genre`, `nombre_exemplaires`) VALUES
(1, 'Bases de données', 'C. Date', 2015, 1, 5),
(2, 'Analyse réelle', 'W. Rudin', 2005, 2, 2),
(3, 'Les Misérables', 'V. Hugo', 1862, 3, 4),
(4, 'Histoire de l\'Afrique', 'J. Ki-Zerbo', 1978, 4, 2),
(5, 'Cosmos', 'Carl Sagan', 1980, 5, 4);

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `emprunts`
--
ALTER TABLE `emprunts`
  ADD PRIMARY KEY (`id_emprunt`),
  ADD KEY `idx_emprunts_etudiant` (`id_etudiant`),
  ADD KEY `idx_emprunts_livre` (`id_livre`),
  ADD KEY `idx_emprunts_retour` (`date_retour`);

--
-- Index pour la table `etudiants`
--
ALTER TABLE `etudiants`
  ADD PRIMARY KEY (`id_etudiant`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Index pour la table `genres`
--
ALTER TABLE `genres`
  ADD PRIMARY KEY (`id_genre`),
  ADD UNIQUE KEY `nom_genre` (`nom_genre`);

--
-- Index pour la table `livres`
--
ALTER TABLE `livres`
  ADD PRIMARY KEY (`id_livre`),
  ADD KEY `idx_livres_genre` (`id_genre`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `emprunts`
--
ALTER TABLE `emprunts`
  MODIFY `id_emprunt` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT pour la table `etudiants`
--
ALTER TABLE `etudiants`
  MODIFY `id_etudiant` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT pour la table `genres`
--
ALTER TABLE `genres`
  MODIFY `id_genre` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT pour la table `livres`
--
ALTER TABLE `livres`
  MODIFY `id_livre` bigint NOT NULL AUTO_INCREMENT;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `emprunts`
--
ALTER TABLE `emprunts`
  ADD CONSTRAINT `fk_emprunts_etudiant` FOREIGN KEY (`id_etudiant`) REFERENCES `etudiants` (`id_etudiant`) ON DELETE RESTRICT ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_emprunts_livre` FOREIGN KEY (`id_livre`) REFERENCES `livres` (`id_livre`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Contraintes pour la table `livres`
--
ALTER TABLE `livres`
  ADD CONSTRAINT `fk_livres_genre` FOREIGN KEY (`id_genre`) REFERENCES `genres` (`id_genre`) ON DELETE RESTRICT ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
