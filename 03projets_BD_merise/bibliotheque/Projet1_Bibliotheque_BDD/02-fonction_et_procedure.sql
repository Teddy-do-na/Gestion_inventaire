

-- Fichier SQL pour les procédures et fonctions en MySQL
-- La fonction pour vérifier la disponibilité d'un livre

-- Change le délimiteur pour la définition de la fonction
DELIMITER //

-- Fonction : disponibilité d'un livre (nombre d'exemplaires disponibles)
CREATE FUNCTION fn_disponibilite_livre(p_id_livre BIGINT)
RETURNS INT
DETERMINISTIC
BEGIN
DECLARE v_total INT;
DECLARE v_en_cours INT;

SELECT nombre_exemplaires INTO v_total
FROM livres WHERE id_livre = p_id_livre;

IF v_total IS NULL THEN
RETURN NULL;
END IF;

SELECT COUNT(*) INTO v_en_cours
FROM emprunts
WHERE id_livre = p_id_livre
AND date_retour IS NULL;

RETURN GREATEST(v_total - v_en_cours, 0);
END //

-- Remet le délimiteur par défaut
DELIMITER ;




-- 2) Procédure: ajouter un nouvel emprunt (cohérence + disponibilité)


DELIMITER $$

CREATE PROCEDURE pr_ajouter_emprunt(
IN p_id_etudiant INT,
IN p_id_livre BIGINT,
IN p_date_emprunt DATE
)
BEGIN
DECLARE v_exists_etud INT;
DECLARE v_exists_livre INT;
DECLARE v_dispo INT;
-- Vérifie l'existence de l'étudiant
SELECT COUNT(*)
INTO v_exists_etud
FROM etudiants
WHERE id_etudiant = p_id_etudiant;

IF v_exists_etud = 0 THEN
SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Etudiant inexistant';
END IF;

-- Vérifie l'existence du livre
SELECT COUNT(*)
INTO v_exists_livre
FROM livres
WHERE id_livre = p_id_livre;
IF v_exists_livre = 0 THEN
SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Livre inexistant';
END IF;

-- Vérifie la disponibilité du livre
-- Cette partie simule la logique de la fonction fn_disponibilite_livre
-- en comptant les exemplaires disponibles par rapport aux emprunts en cours.
SELECT (l.nombre_exemplaires - COUNT(e.id_livre))
INTO v_dispo
FROM livres l
LEFT JOIN emprunts e ON l.id_livre = e.id_livre AND e.date_retour IS NULL
WHERE l.id_livre = p_id_livre
GROUP BY l.id_livre;

IF v_dispo IS NULL OR v_dispo <= 0 THEN
SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Livre indisponible';
END IF;

-- Ajout du nouvel emprunt
INSERT INTO emprunts(id_etudiant, id_livre, date_emprunt, date_retour)
VALUES (p_id_etudiant, p_id_livre, COALESCE(p_date_emprunt, CURRENT_DATE), NULL);
END$$

DELIMITER ;


