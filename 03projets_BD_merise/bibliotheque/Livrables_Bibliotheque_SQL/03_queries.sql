-- 03_queries.sql
USE bibliotheque;

-- 1) Lister tous les livres d'un certain genre
-- Remplacer 'Science-fiction' par le genre voulu
SELECT l.*
FROM Livres l
JOIN Genres g ON g.id_genre = l.id_genre
WHERE g.nom_genre = 'Science-fiction';

-- 2) Étudiants ayant emprunté un certain livre (par titre)
-- Remplacer 'Dune' par le titre voulu
SELECT DISTINCT s.*
FROM Emprunts e
JOIN Etudiants s ON s.id_etudiant = e.id_etudiant
JOIN Livres l ON l.id_livre = e.id_livre
WHERE l.titre = 'Dune';

-- 3) Emprunts en cours d'un étudiant (non retournés)
-- Remplacer 1 par l'id de l'étudiant
SELECT e.*
FROM Emprunts e
WHERE e.id_etudiant = 1
  AND e.date_retour IS NULL;

-- 4) Nombre total d'emprunts par livre
SELECT l.titre, COUNT(*) AS nb_emprunts
FROM Emprunts e
JOIN Livres l ON l.id_livre = e.id_livre
GROUP BY l.id_livre, l.titre
ORDER BY nb_emprunts DESC;