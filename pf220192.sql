-- CREATE DATABASE SAB_PROJECT; GO

USE SAB_PROJECT;
GO

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

DELETE FROM FilmZanrLink;
DELETE FROM FilmOznakaLink;
DELETE FROM Lista;
DELETE FROM Ocena;
DELETE FROM Zanr;
DELETE FROM Oznaka;
DELETE FROM Film;
DELETE FROM Korisnik;

DBCC CHECKIDENT ('FilmZanrLink', RESEED, 1);
DBCC CHECKIDENT ('FilmOznakaLink', RESEED, 1);
DBCC CHECKIDENT ('Lista', RESEED, 1);
DBCC CHECKIDENT ('Ocena', RESEED, 1);
DBCC CHECKIDENT ('Zanr', RESEED, 1);
DBCC CHECKIDENT ('Oznaka', RESEED, 1);
DBCC CHECKIDENT ('Film', RESEED, 1);
DBCC CHECKIDENT ('Korisnik', RESEED, 1);


INSERT INTO Korisnik (ime, brojNagrada) VALUES 
('fili5', 0),        -- idKor = 1, bice NEDEFINISAN (3 filma)
('jana', 0),         -- idKor = 2, bice FOKUSIRAN (10 filmova, 4 oznake)
('cikaJoza', 0),     -- idKor = 3, bice NEDEFINISAN (0 filmova)
('xGogax', 0),       -- idKor = 4, bice RADOZNAO (10 filmova, 10+ oznaka)
('brale', 0),
('vexa', 0),
('skibidi', 0),
('djape308', 0);

INSERT INTO Film (naslov, reziser) VALUES 
('The Shawshank Redemption', 'Frank Darabont'),      -- idFilm = 1
('The Matrix', 'Lana i Lilly Wachowski'),            -- idFilm = 2
('Fight Club', 'David Fincher'),                     -- idFilm = 3
('Superbad', 'Greg Mottola'),                        -- idFilm = 4
('The Hangover', 'Todd Phillips'),                   -- idFilm = 5
('21 Jump Street', 'Phil Lord, Christopher Miller'), -- idFilm = 6
('Ted', 'Seth MacFarlane'),                          -- idFilm = 7
('EuroTrip', 'Jeff Schaffer'),                       -- idFilm = 8
('Project X', 'Nima Nourizadeh'),                    -- idFilm = 9
('We''re the Millers', 'Rawson Marshall Thurber'),   -- idFilm = 10
('Inception', 'Christopher Nolan'),                  -- idFilm = 11
('Interstellar', 'Christopher Nolan'),               -- idFilm = 12
('The Dark Knight', 'Christopher Nolan'),            -- idFilm = 13
('Pulp Fiction', 'Quentin Tarantino'),               -- idFilm = 14
('Forrest Gump', 'Robert Zemeckis');                 -- idFilm = 15

INSERT INTO Zanr (naziv) VALUES 
('Drama'),
('Komedija'),
('Akcija'),
('Avantura'),
('Triler'),
('Horor'),
('Fantazija'),
('Naucna fantastika'),
('Misterija'),
('Krimi'),
('Romantika'),
('Muzicki'),
('Sport'),
('Ratni'),
('Istorijski'),
('Biografski'),
('Dokumentarni'),
('Vestern'),
('Porodicni'),
('Animirani');

INSERT INTO Oznaka (oznaka) VALUES 
('porodica'),        -- idOznaka = 1
('ljubav'),          -- idOznaka = 2
('prijateljstvo'),   -- idOznaka = 3
('zabava'),          -- idOznaka = 4
('partijanje'),      -- idOznaka = 5
('putovanje'),       -- idOznaka = 6
('tajna'),           -- idOznaka = 7
('opasnost'),        -- idOznaka = 8
('pobuna'),          -- idOznaka = 9
('heroji'),          -- idOznaka = 10
('magija'),          -- idOznaka = 11
('buducnost'),       -- idOznaka = 12
('realna-prica'),    -- idOznaka = 13
('muzika'),          -- idOznaka = 14
('sport'),           -- idOznaka = 15
('nasilje'),         -- idOznaka = 16
('detinjstvo'),      -- idOznaka = 17
('prevara'),         -- idOznaka = 18
('sloboda'),         -- idOznaka = 19
('pravda');          -- idOznaka = 20

INSERT INTO FilmZanrLink (idFilm, idZanr) VALUES 
(1, 1), (1, 10),
(2, 3), (2, 8),
(3, 1), (3, 5),
(4, 2), (4, 11),
(5, 2), (5, 4),
(6, 2), (6, 3),
(7, 2), (7, 11),
(8, 2), (8, 4),
(9, 2), (9, 4),
(10, 2), (10, 4),
(11, 8), (11, 5),
(12, 8), (12, 1),
(13, 3), (13, 10),
(14, 10), (14, 5),
(15, 1), (15, 11);

-- Povezujemo filmove sa oznakama - PAZLJIVO DIZAJNIRANO
-- Film 1,2,3 imaju oznaku 2 (ljubav)
-- Film 4,6 imaju oznaku 3 (prijateljstvo)
-- Film 5,7,8 imaju oznaku 4 (zabava)
-- Film 5,9,10 imaju oznaku 5 (partijanje)
-- Film 11,12,13,14,15 imaju razne druge oznake za "radoznao" slucaj
INSERT INTO FilmOznakaLink (idFilm, idOznaka) VALUES
(1, 2),   -- Shawshank > ljubav
(2, 2),   -- Matrix > ljubav
(3, 2),   -- Fight Club > ljubav
(4, 3),   -- Superbad > prijateljstvo
(5, 4),   -- Hangover > zabava
(5, 5),   -- Hangover > partijanje
(6, 3),   -- 21 Jump Street > prijateljstvo
(7, 4),   -- Ted > zabava
(8, 4),   -- EuroTrip > zabava
(9, 5),   -- Project X > partijanje
(10, 5),  -- We're the Millers > partijanje
(11, 7),  -- Inception > tajna
(11, 12), -- Inception > buducnost
(12, 12), -- Interstellar > buducnost
(12, 1),  -- Interstellar > porodica
(13, 8),  -- Dark Knight > opasnost
(13, 20), -- Dark Knight > pravda
(14, 16), -- Pulp Fiction > nasilje
(14, 18), -- Pulp Fiction > prevara
(15, 1),  -- Forrest Gump > porodica
(15, 13); -- Forrest Gump > realna-prica

-- ============================================
-- OCENE - DIZAJNIRANE ZA 3 RAZLIcITA SLUcAJA
-- ============================================

-- KORISNIK 1 (fili5): NEDEFINISAN - samo 3 filma (< 10)
INSERT INTO Ocena (idKor, idFilm, ocena) VALUES
(1, 1, 9.0),
(1, 2, 8.5),
(1, 3, 7.0);

-- KORISNIK 2 (jana): FOKUSIRAN - 10 filmova, tacno 4 razlicite oznake
-- Gleda samo filmove sa oznakama: ljubav(2), prijateljstvo(3), zabava(4), partijanje(5)
INSERT INTO Ocena (idKor, idFilm, ocena) VALUES
(2, 1, 8.0),   -- ljubav (2)
(2, 2, 7.0),   -- ljubav (2)
(2, 3, 9.0),   -- ljubav (2)
(2, 4, 6.0),   -- prijateljstvo (3)
(2, 6, 8.0),   -- prijateljstvo (3)
(2, 5, 7.0),   -- zabava (4) + partijanje (5)
(2, 7, 9.0),   -- zabava (4)
(2, 8, 6.0),   -- zabava (4)
(2, 9, 8.0),   -- partijanje (5)
(2, 10, 7.0);  -- partijanje (5)
-- Ukupno 4 razlicite oznake: 2, 3, 4, 5

-- KORISNIK 4 (xGogax): RADOZNAO - 10 filmova, 12 razlicitih oznaka
INSERT INTO Ocena (idKor, idFilm, ocena) VALUES
(4, 1, 9.0),   -- ljubav (2)
(4, 4, 6.0),   -- prijateljstvo (3)
(4, 7, 7.0),   -- zabava (4)
(4, 9, 8.0),   -- partijanje (5)
(4, 11, 7.0),  -- tajna (7) + buducnost (12)
(4, 12, 8.0),  -- buducnost (12) + porodica (1)
(4, 13, 6.0),  -- opasnost (8) + pravda (20)
(4, 14, 9.0),  -- nasilje (16) + prevara (18)
(4, 15, 8.5),  -- porodica (1) + realna-prica (13)
(4, 2, 7.5);   -- ljubav (2)
-- Razlicite oznake: 2, 3, 4, 5, 7, 12, 1, 8, 20, 16, 18, 13 = 12 oznaka

-- Lista - proizvoljno
INSERT INTO Lista (idKor, idFilm) VALUES 
(1, 5),
(2, 11),
(3, 1);

-- ============================================
-- PROVERA PODATAKA
-- ============================================
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
SELECT 'Ocena', COUNT(*) FROM Ocena;