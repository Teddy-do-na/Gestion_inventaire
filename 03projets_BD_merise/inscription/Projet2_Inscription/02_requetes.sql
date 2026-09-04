-- 03_requetes.sql (MYSQL)

-- 1) Lister tous les étudiants inscrits à une formation
SELECT s.id_etudiant, s.nom, s.prenom, s.email, i.date_inscription, i.statut_paiement
FROM inscriptions i
JOIN etudiants s ON s.id_etudiant = i.id_etudiant
JOIN formations f ON f.id_formation = i.id_formation
WHERE f.nom_formation = 'DevOps Essentials'
ORDER BY s.nom, s.prenom;

-- 2) Trouver les paiements d’un étudiant donné
SELECT p.id_paiement, f.nom_formation, p.montant, p.date_paiement, p.methode_paiement
FROM paiements p
JOIN inscriptions i ON i.id_inscription = p.id_inscription
JOIN etudiants s ON s.id_etudiant = i.id_etudiant
JOIN formations f ON f.id_formation = i.id_formation
WHERE s.email = 'jordannandjo@yahoo.com'
ORDER BY p.date_paiement DESC;

-- 3) Lister les inscriptions en cours (statut 'En attente') d’un étudiant
SELECT i.id_inscription, f.nom_formation, i.date_inscription, i.statut_paiement
FROM inscriptions i
JOIN etudiants s ON s.id_etudiant = i.id_etudiant
JOIN formations f ON f.id_formation = i.id_formation
WHERE s.email = 'estelle.ngono@univ.example'
  AND i.statut_paiement = 'En attente'
ORDER BY i.date_inscription DESC;

-- 4) Compter le nombre d’inscriptions par formation
SELECT f.id_formation, f.nom_formation, COUNT(i.id_inscription) AS nb_inscriptions
FROM formations f
LEFT JOIN inscriptions i ON i.id_formation = f.id_formation
GROUP BY f.id_formation, f.nom_formation
ORDER BY nb_inscriptions DESC, f.nom_formation;

-- ======= Manipulations demandées =======

-- UPDATE formation (ex: modifier tarif)
UPDATE formations
   SET tarif = 175000
 WHERE nom_formation = 'Data Science 101';

-- UPDATE étudiant (ex: numéro de téléphone)
UPDATE etudiants
   SET telephone = '(+237)690000099'
 WHERE email = 'marc.ndongo@univ.example';

-- DELETE une inscription (ex: par email + formation)
DELETE FROM inscriptions
 WHERE id_inscription IN (
   SELECT i.id_inscription
   FROM inscriptions i
   JOIN etudiants e  ON e.id_etudiant  = i.id_etudiant
   JOIN formations f ON f.id_formation = i.id_formation
   WHERE e.email='estelle.ngono@univ.example'
     AND f.nom_formation='Web Full-Stack'
 );
