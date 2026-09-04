-- 05_views_triggers.sql
USE bibliotheque;
DELIMITER $$

-- Vue: détails des emprunts
DROP VIEW IF EXISTS v_emprunts_details $$
CREATE VIEW v_emprunts_details AS
SELECT e.id_emprunt,
       l.id_livre,
       l.titre,
       CONCAT(s.nom, ' ', s.prenom) AS etudiant,
       e.date_emprunt,
       e.date_retour
FROM Emprunts e
JOIN Livres l ON l.id_livre = e.id_livre
JOIN Etudiants s ON s.id_etudiant = e.id_etudiant $$

-- Trigger: avant insertion d'un emprunt -> contrôle + décrément
DROP TRIGGER IF EXISTS trg_emprunt_before_insert $$
CREATE TRIGGER trg_emprunt_before_insert
BEFORE INSERT ON Emprunts
FOR EACH ROW
BEGIN
  DECLARE dispo INT;
  SELECT exemplaires_disponibles INTO dispo FROM Livres WHERE id_livre = NEW.id_livre FOR UPDATE;
  IF dispo IS NULL THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Livre inexistant';
  END IF;
  IF dispo <= 0 THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Aucun exemplaire disponible';
  END IF;
  UPDATE Livres SET exemplaires_disponibles = exemplaires_disponibles - 1 WHERE id_livre = NEW.id_livre;
END $$

-- Trigger: après mise à jour -> si retour, incrément
DROP TRIGGER IF EXISTS trg_emprunt_after_update $$
CREATE TRIGGER trg_emprunt_after_update
AFTER UPDATE ON Emprunts
FOR EACH ROW
BEGIN
  IF OLD.date_retour IS NULL AND NEW.date_retour IS NOT NULL THEN
    UPDATE Livres SET exemplaires_disponibles = exemplaires_disponibles + 1 WHERE id_livre = NEW.id_livre;
  END IF;
END $$

DELIMITER ;