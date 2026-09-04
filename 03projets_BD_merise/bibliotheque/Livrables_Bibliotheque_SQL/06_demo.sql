-- 06_demo.sql
USE bibliotheque;

-- Démo: emprunt via procédure
CALL sp_ajouter_emprunt(1, (SELECT id_livre FROM Livres WHERE titre='Dune'));

-- Vérifier la disponibilité après emprunt
SELECT titre, exemplaires_disponibles FROM Livres WHERE titre='Dune';

-- Enregistrer un retour via procédure
CALL sp_enregistrer_retour((
  SELECT e.id_emprunt FROM Emprunts e
  JOIN Livres l ON l.id_livre = e.id_livre
  WHERE e.id_etudiant = 1 AND l.titre = 'Dune'
  ORDER BY e.id_emprunt DESC LIMIT 1
));

-- Vérifier la disponibilité après retour
SELECT titre, exemplaires_disponibles FROM Livres WHERE titre='Dune';

-- Requêtes exemple (rappels)
SOURCE 03_queries.sql;