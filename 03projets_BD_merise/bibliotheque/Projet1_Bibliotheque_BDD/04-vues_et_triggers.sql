
-- 05_vues_triggers.sql (MYSQL)
-- Vue de détails + triggers d'ajustement d'exemplaires

-- Vue
CREATE OR REPLACE VIEW vw_details_emprunts AS
SELECT
  em.id_emprunt,
  l.id_livre,
  l.titre AS livre,
  (e.nom || ' ' || e.prenom) AS etudiant,
  e.email,
  em.date_emprunt,
  em.date_retour
FROM emprunts em
JOIN livres l ON l.id_livre = em.id_livre
JOIN etudiants e ON e.id_etudiant = em.id_etudiant;



-- Drop existing triggers to allow for re-creation
DROP TRIGGER IF EXISTS trg_emprunt_after_insert;
DROP TRIGGER IF EXISTS trg_emprunt_after_update;

---

-- Trigger executed after an INSERT on the `emprunts` table
DELIMITER $$
CREATE TRIGGER trg_emprunt_after_insert
AFTER INSERT ON emprunts
FOR EACH ROW
BEGIN
-- If the loan is new and has no return date (meaning it's in progress)
IF NEW.date_retour IS NULL THEN
-- Decrement the number of available copies for the book
UPDATE livres
SET nombre_exemplaires = nombre_exemplaires - 1
WHERE id_livre = NEW.id_livre;

-- Check if the stock has become negative and raise an error if it has
IF (SELECT nombre_exemplaires FROM livres WHERE id_livre = NEW.id_livre) < 0 THEN
SIGNAL SQLSTATE '45000'
SET MESSAGE_TEXT = 'Negative book stock is not allowed.';
END IF;
END IF;
END$$
DELIMITER ;

---

-- Trigger executed after an UPDATE on the `emprunts` table
DELIMITER $$
CREATE TRIGGER trg_emprunt_after_update
AFTER UPDATE ON emprunts
FOR EACH ROW
BEGIN
-- If the book is being returned (return date changes from NULL to a value)
IF OLD.date_retour IS NULL AND NEW.date_retour IS NOT NULL THEN
-- Increment the number of available copies for the book
UPDATE livres
SET nombre_exemplaires = nombre_exemplaires + 1
WHERE id_livre = NEW.id_livre;
END IF;
END$$
DELIMITER ;


