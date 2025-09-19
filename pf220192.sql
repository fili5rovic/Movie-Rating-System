DROP TABLE IF EXISTS KorisnikSpecijalizacija;
DROP TABLE IF EXISTS FilmZanrLink;
DROP TABLE IF EXISTS FilmOznakaLink;
DROP TABLE IF EXISTS Lista;
DROP TABLE IF EXISTS Ocena;
DROP TABLE IF EXISTS Zanr;
DROP TABLE IF EXISTS Oznaka;
DROP TABLE IF EXISTS Film;
DROP TABLE IF EXISTS Korisnik;

CREATE TABLE Korisnik (
    idKor INT IDENTITY PRIMARY KEY,
    ime NVARCHAR(100) NOT NULL,
    brojNagrada INT DEFAULT 0
);

CREATE TABLE Film (
    idFilm INT IDENTITY PRIMARY KEY,
    naslov NVARCHAR(100) NOT NULL,
    reziser NVARCHAR(100) NOT NULL
);

CREATE TABLE Oznaka (
    idOznaka INT IDENTITY PRIMARY KEY,
    oznaka NVARCHAR(100) NOT NULL
);

CREATE TABLE Zanr (
    idZanr INT IDENTITY PRIMARY KEY,
    naziv NVARCHAR(100) NOT NULL
);

CREATE TABLE Ocena (
    idOcena INT IDENTITY PRIMARY KEY,
    idKor INT NOT NULL,
    idFilm INT NOT NULL,
    ocena DECIMAL(10,3) NOT NULL,

    CONSTRAINT CHK_Ocena_Range CHECK (ocena >= 1 AND ocena <= 10),
    CONSTRAINT FK_Ocena_Korisnik FOREIGN KEY (idKor) REFERENCES Korisnik(idKor) ON UPDATE CASCADE ON DELETE NO ACTION,
    CONSTRAINT FK_Ocena_Film FOREIGN KEY (idFilm) REFERENCES Film(idFilm) ON UPDATE CASCADE ON DELETE NO ACTION,
    CONSTRAINT UQ_Ocena_KorFilm UNIQUE (idKor, idFilm)
);

CREATE TABLE Lista (
    idLista INT IDENTITY PRIMARY KEY,
    idKor INT NOT NULL,
    idFilm INT NOT NULL,

    CONSTRAINT FK_Lista_Korisnik FOREIGN KEY (idKor) REFERENCES Korisnik(idKor) ON UPDATE CASCADE ON DELETE NO ACTION,
    CONSTRAINT FK_Lista_Film FOREIGN KEY (idFilm) REFERENCES Film(idFilm) ON UPDATE CASCADE ON DELETE NO ACTION,
    CONSTRAINT UQ_Lista_KorFilm UNIQUE (idKor, idFilm)
);

CREATE TABLE FilmOznakaLink (
    idLink INT IDENTITY PRIMARY KEY,
    idFilm INT NOT NULL,
    idOznaka INT NOT NULL,
    CONSTRAINT FK_FilmOznaka_Film FOREIGN KEY (idFilm) REFERENCES Film(idFilm) ON UPDATE CASCADE ON DELETE NO ACTION,
    CONSTRAINT FK_FilmOznaka_Oznaka FOREIGN KEY (idOznaka) REFERENCES Oznaka(idOznaka) ON UPDATE CASCADE ON DELETE NO ACTION,
    CONSTRAINT UQ_FilmOznaka UNIQUE (idFilm, idOznaka)
);

CREATE TABLE FilmZanrLink (
    idLink INT IDENTITY PRIMARY KEY,
    idFilm INT NOT NULL,
    idZanr INT NOT NULL,
    CONSTRAINT FK_FilmZanr_Film FOREIGN KEY (idFilm) REFERENCES Film(idFilm) ON UPDATE CASCADE ON DELETE NO ACTION,
    CONSTRAINT FK_FilmZanr_Zanr FOREIGN KEY (idZanr) REFERENCES Zanr(idZanr) ON UPDATE CASCADE ON DELETE NO ACTION,
    CONSTRAINT UQ_FilmZanr UNIQUE (idFilm, idZanr)
);

CREATE TABLE KorisnikSpecijalizacija (
    idSpecijalizacija INT IDENTITY PRIMARY KEY,
    idKor INT NOT NULL,
    idOznaka INT NOT NULL,
    
    CONSTRAINT FK_KS_Korisnik FOREIGN KEY (idKor) REFERENCES Korisnik(idKor) ON UPDATE CASCADE ON DELETE NO ACTION,
    CONSTRAINT FK_KS_Oznaka FOREIGN KEY (idOznaka) REFERENCES Oznaka(idOznaka) ON UPDATE CASCADE ON DELETE NO ACTION,
    CONSTRAINT UQ_KS_KorOznaka UNIQUE (idKor, idOznaka)
);

DELETE FROM KorisnikSpecijalizacija;
DELETE FROM FilmZanrLink;
DELETE FROM FilmOznakaLink;
DELETE FROM Lista;
DELETE FROM Ocena;
DELETE FROM Zanr;
DELETE FROM Oznaka;
DELETE FROM Film;
DELETE FROM Korisnik;

DBCC CHECKIDENT ('KorisnikSpecijalizacija', RESEED, 1);
DBCC CHECKIDENT ('FilmZanrLink', RESEED, 1);
DBCC CHECKIDENT ('FilmOznakaLink', RESEED, 1);
DBCC CHECKIDENT ('Lista', RESEED, 1);
DBCC CHECKIDENT ('Ocena', RESEED, 1);
DBCC CHECKIDENT ('Zanr', RESEED, 1);
DBCC CHECKIDENT ('Oznaka', RESEED, 1);
DBCC CHECKIDENT ('Film', RESEED, 1);
DBCC CHECKIDENT ('Korisnik', RESEED, 1);


INSERT INTO Korisnik (ime, brojNagrada) VALUES 
('marko123', 0),
('ana_film', 2),
('petar2024', 1);

INSERT INTO Zanr (naziv) VALUES 
('Drama'),
('Komedija'),
('Akcija');

INSERT INTO Oznaka (oznaka) VALUES 
('porodica'),
('ljubav'),
('prijateljstvo');

INSERT INTO Film (naslov, reziser) VALUES 
('Titanik', 'James Cameron'),
('Matrix', 'Wachowski Brothers'),
('Shrek', 'Andrew Adamson');

INSERT INTO FilmZanrLink (idFilm, idZanr) VALUES 
(1, 1),
(2, 3),
(3, 2);

INSERT INTO FilmOznakaLink (idFilm, idOznaka) VALUES 
(1, 2),
(2, 3),
(3, 1);

INSERT INTO Lista (idKor, idFilm) VALUES 
(1, 2),
(2, 3),
(3, 1);

INSERT INTO Ocena (idKor, idFilm, ocena) VALUES 
(1, 1, 9),
(2, 2, 8),
(3, 3, 10);

INSERT INTO KorisnikSpecijalizacija (idKor, idOznaka) VALUES 
(1, 2),
(2, 3),
(3, 1);

SELECT 'Korisnik' as tabela, COUNT(*) as broj FROM Korisnik
UNION ALL
SELECT 'Film', COUNT(*) FROM Film
UNION ALL
SELECT 'Zanr', COUNT(*) FROM Zanr
UNION ALL
SELECT 'Oznaka', COUNT(*) FROM Oznaka
UNION ALL
SELECT 'FilmZanrLink', COUNT(*) FROM FilmZanrLink
UNION ALL
SELECT 'FilmOznakaLink', COUNT(*) FROM FilmOznakaLink
UNION ALL
SELECT 'Lista', COUNT(*) FROM Lista
UNION ALL
SELECT 'Ocena', COUNT(*) FROM Ocena
UNION ALL
SELECT 'KorisnikSpecijalizacija', COUNT(*) FROM KorisnikSpecijalizacija;