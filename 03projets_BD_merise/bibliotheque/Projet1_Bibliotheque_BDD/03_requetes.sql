-- 03_requetes.sql (MYSQL)

-- 1) Lister tous les livres d’un genre donné
-- Remplacer 'Informatique' si besoin
SELECT l.id_livre, l.titre, l.auteur, l.annee_publication, g.nom_genre, l.nombre_exemplaires
FROM livres l
JOIN genres g ON g.id_genre = l.id_genre
WHERE g.nom_genre = 'Informatique'
ORDER BY l.titre;

-- 2) Étudiants ayant emprunté un livre donné (par titre exact)
SELECT DISTINCT e.id_etudiant, e.nom, e.prenom, e.email
FROM etudiants e
JOIN emprunts em ON em.id_etudiant = e.id_etudiant
JOIN livres l ON l.id_livre = em.id_livre
WHERE l.titre = 'Les Misérables'
ORDER BY e.nom, e.prenom;

-- 3) Emprunts en cours d’un étudiant (par email)
SELECT em.id_emprunt, l.titre, em.date_emprunt
FROM emprunts em
JOIN etudiants e ON e.id_etudiant = em.id_etudiant
JOIN livres l ON l.id_livre = em.id_livre
WHERE e.email = 'estelle.ngono@etu.univ.example'
  AND em.date_retour IS NULL
ORDER BY em.date_emprunt DESC;

-- 4) Nombre total d’emprunts par livre
SELECT l.id_livre, l.titre, COUNT(em.id_emprunt) AS nb_emprunts
FROM livres l
LEFT JOIN emprunts em ON em.id_livre = l.id_livre
GROUP BY l.id_livre, l.titre
ORDER BY nb_emprunts DESC, l.titre;

-- Exemples MàJ/Suppression
-- Mettre à jour un livre
UPDATE livres SET nombre_exemplaires = 6 WHERE titre = 'Algorithmique avancée';

-- Mettre à jour un étudiant
UPDATE etudiants SET telephone = '+237690000099' WHERE email = 'marc.ndongo@etu.univ.example';

-- Supprimer un emprunt (par id)
DELETE FROM emprunts WHERE id_emprunt = 1;
