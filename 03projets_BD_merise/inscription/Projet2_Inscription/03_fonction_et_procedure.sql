
DELIMITER //
-- Fonction: nombre de places restantes d'une formation CREATE FUNCTION fn_places_restantes(p_id_formation INT) RETURNS INT DETERMINISTIC BEGIN DECLARE v_total INT; DECLARE v_count INT;
SELECT nombre_places INTO v_total
FROM formations WHERE id_formation = p_id_formation;

IF v_total IS NULL THEN
    RETURN NULL;
END IF;

SELECT COUNT(*) INTO v_count
FROM inscriptions
WHERE id_formation = p_id_formation;

RETURN GREATEST(v_total - v_count, 0);
END //
DELIMITER ;


DELIMITER //
-- Procédure : ajouter une nouvelle inscription (contrôles + unicité) CREATE PROCEDURE pr_ajouter_inscription( IN p_id_etudiant INT, IN p_id_formation INT ) BEGIN DECLARE v_etud_exists INT; DECLARE v_form_exists INT; DECLARE v_dispo INT;
SELECT COUNT(*) INTO v_etud_exists
FROM etudiants WHERE id_etudiant = p_id_etudiant;

IF v_etud_exists = 0 THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Etudiant inexistant';
END IF;

SELECT COUNT(*) INTO v_form_exists
FROM formations WHERE id_formation = p_id_formation;

IF v_form_exists = 0 THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Formation inexistante';
END IF;

SET v_dispo = fn_places_restantes(p_id_formation);

IF v_dispo <= 0 THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Aucune place disponible pour la formation';
END IF;

INSERT IGNORE INTO inscriptions (id_etudiant, id_formation, date_inscription, statut_paiement)
VALUES (p_id_etudiant, p_id_formation, CURRENT_DATE(), 'En attente');
END //
DELIMITER ;

