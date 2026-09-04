-- Vue des détails d'inscriptions
CREATE OR REPLACE VIEW vw_details_inscriptions AS
SELECT
i.id_inscription,
f.id_formation,
f.nom_formation,
CONCAT(e.nom, ' ', e.prenom) AS etudiant,
e.email,
i.date_inscription,
i.statut_paiement
FROM inscriptions i
JOIN formations f ON f.id_formation = i.id_formation
JOIN etudiants e ON e.id_etudiant = i.id_etudiant;

-- Trigger: mise à jour du statut de paiement à l'INSERT d'un paiement
DELIMITER //

CREATE TRIGGER trg_paiement_update_statut
AFTER INSERT ON paiements
FOR EACH ROW
BEGIN
UPDATE inscriptions
SET statut_paiement = 'Payé'
WHERE id_inscription = NEW.id_inscription;
END;
//

DELIMITER ;


