-- 04_routines.sql
USE bibliotheque;
DELIMITER $$

-- Fonction: vérifier la disponibilité d'un livre
DROP FUNCTION IF EXISTS fn_livre_disponible $$
CREATE FUNCTION fn_livre_disponible(p_id_livre INT)
RETURNS TINYINT
DETERMINISTIC
READS SQL DATA
BEGIN
  DECLARE dispo INT;
  SELECT exemplaires_disponibles INTO dispo
  FROM Livres
  WHERE id_livre = p_id_livre;
  RETURN IFNULL(dispo,0) > 0;
END $$

-- Procédure: ajouter un nouvel emprunt
DROP PROCEDURE IF EXISTS sp_ajouter_emprunt $$
CREATE PROCEDURE sp_ajouter_emprunt(IN p_id_etudiant INT, IN p_id_livre INT)
BEGIN
  -- Existence
  IF NOT EXISTS (SELECT 1 FROM Etudiants WHERE id_etudiant = p_id_etudiant) THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Etudiant inexistant';
  END IF;
  IF NOT EXISTS (SELECT 1 FROM Livres WHERE id_livre = p_id_livre) THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Livre inexistant';
  END IF;

  -- Disponibilité
  IF fn_livre_disponible(p_id_livre) = 0 THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Aucun exemplaire disponible';
  END IF;

  -- Insertion (le trigger décrémente la dispo)
  INSERT INTO Emprunts(id_etudiant, id_livre, date_emprunt) VALUES (p_id_etudiant, p_id_livre, NOW());
END $$

-- Procédure: enregistrer un retour
DROP PROCEDURE IF EXISTS sp_enregistrer_retour $$
CREATE PROCEDURE sp_enregistrer_retour(IN p_id_emprunt INT)
BEGIN
  IF NOT EXISTS (SELECT 1 FROM Emprunts WHERE id_emprunt = p_id_emprunt) THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Emprunt inexistant';
  END IF;

  UPDATE Emprunts
  SET date_retour = NOW()
  WHERE id_emprunt = p_id_emprunt AND date_retour IS NULL;

  IF ROW_COUNT() = 0 THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Emprunt déjà retourné ou inexistant';
  END IF;
END $$

DELIMITER ;