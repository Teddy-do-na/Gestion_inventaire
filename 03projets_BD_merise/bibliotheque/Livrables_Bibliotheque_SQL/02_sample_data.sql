-- 02_sample_data.sql
USE bibliotheque;

-- Genres
INSERT INTO Genres (nom_genre) VALUES
('Science-fiction'),
('Roman'),
('Histoire'),
('Informatique');

-- Etudiants
INSERT INTO Etudiants (nom, prenom, date_naissance, email, telephone)
VALUES 
('Sikati','Jacky','2002-05-12','jacky@example.com','+237600000001'),
('Doe','Jane','2001-03-02','jane.doe@example.com','+237600000002'),
('Ngono','Paul','2000-11-21','paul.ngono@example.com','+237600000003');

-- Livres
INSERT INTO Livres (titre, auteur, annee_publication, id_genre, nombre_exemplaires, exemplaires_disponibles)
VALUES
('Dune','Frank Herbert',1965,(SELECT id_genre FROM Genres WHERE nom_genre='Science-fiction'),5,5),
('1984','George Orwell',1949,(SELECT id_genre FROM Genres WHERE nom_genre='Roman'),3,3),
('Sapiens','Yuval Noah Harari',2011,(SELECT id_genre FROM Genres WHERE nom_genre='Histoire'),4,4),
('Database System Concepts','Silberschatz',2019,(SELECT id_genre FROM Genres WHERE nom_genre='Informatique'),2,2);