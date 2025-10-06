CREATE OR ALTER PROCEDURE SP_RECOMMENDATION
    @id INT
AS
BEGIN
    SET NOCOUNT ON;

    ;WITH OmiljeniZanrovi AS (
        SELECT z.idZanr
        FROM Ocena o
        JOIN FilmZanrLink fzl ON o.idFilm = fzl.idFilm
        JOIN Zanr z ON fzl.idZanr = z.idZanr
        WHERE o.idKor = @id
        GROUP BY z.idZanr
        HAVING AVG(o.ocena) >= 8
    ),
    FilmStats AS (
        SELECT f.idFilm, f.naslov, COUNT(o.idOcena) AS brojOcena, AVG(o.ocena) AS avgOcena
        FROM Film f
        LEFT JOIN Ocena o ON f.idFilm = o.idFilm
        GROUP BY f.idFilm, f.naslov
    ),
    FilmZaPreporuku AS (
        SELECT DISTINCT f.idFilm, f.naslov, fs.avgOcena
        FROM Film f
        JOIN FilmZanrLink fzl ON f.idFilm = fzl.idFilm
        JOIN OmiljeniZanrovi oz ON fzl.idZanr = oz.idZanr
        JOIN FilmStats fs ON f.idFilm = fs.idFilm
        WHERE f.idFilm NOT IN (SELECT idFilm FROM Ocena WHERE idKor = @id)
          AND f.idFilm NOT IN (SELECT idFilm FROM Lista WHERE idKor = @id)
          AND (
              (fs.brojOcena >= 4 AND fs.avgOcena >= 7.5)
              OR
              (fs.brojOcena < 4 AND fs.avgOcena >= 9)
          )
    )
    SELECT idFilm, naslov, avgOcena
    FROM FilmZaPreporuku
    ORDER BY avgOcena DESC, idFilm ASC;
END
GO

-- EXEC SP_RECOMMENDATION 1;

CREATE OR ALTER TRIGGER TR_BLOCK_EXTREME_RATINGS
ON Ocena
AFTER INSERT, UPDATE
AS
BEGIN
    SET NOCOUNT ON;

    IF EXISTS (
        SELECT 1
        FROM inserted i
        CROSS APPLY (
            SELECT fzl.idZanr
            FROM FilmZanrLink fzl
            WHERE fzl.idFilm = i.idFilm
        ) AS f
        CROSS APPLY (
            SELECT COUNT(*) AS brojEkstremnih
            FROM Ocena o
            JOIN FilmZanrLink fzl2 ON o.idFilm = fzl2.idFilm
            WHERE o.idKor = i.idKor
              AND fzl2.idZanr = f.idZanr
              AND o.ocena IN (1,10)
        ) AS ek
        CROSS APPLY (
            SELECT COUNT(*) AS brojNeutralnih
            FROM Ocena o
            JOIN FilmZanrLink fzl2 ON o.idFilm = fzl2.idFilm
            WHERE o.idKor = i.idKor
              AND fzl2.idZanr = f.idZanr
              AND o.ocena IN (6,7,8)
        ) AS neu
        WHERE i.ocena IN (1,10)
          AND ek.brojEkstremnih > 3
          AND neu.brojNeutralnih < 3
    )
    BEGIN
        RAISERROR('Ne mozete dati ekstremnu ocenu u ovom zanru dok ne popravite svoje prethodne ocene.',16,1)
        ROLLBACK TRANSACTION;
        RETURN;
    END
END
GO

CREATE OR ALTER PROCEDURE SP_REWARD_USER_PROC
    @idKor INT
AS
BEGIN
    SET NOCOUNT ON;

    SELECT o.idFilm INTO #FilmZaNagradu
    FROM Ocena o
    JOIN FilmZanrLink fzl ON o.idFilm = fzl.idFilm
    JOIN (
        SELECT z.idZanr
        FROM Ocena o2
        JOIN FilmZanrLink fzl2 ON o2.idFilm = fzl2.idFilm
        JOIN Zanr z ON fzl2.idZanr = z.idZanr
        WHERE o2.idKor = @idKor
        GROUP BY z.idZanr
        HAVING AVG(o2.ocena) >= 8
    ) oz ON fzl.idZanr = oz.idZanr
    CROSS APPLY (
        SELECT AVG(o3.ocena*1.0) AS globalAvg
        FROM Ocena o3
        WHERE o3.idFilm = o.idFilm AND o3.idKor <> @idKor
    ) ga
    WHERE o.idKor = @idKor
      AND (ga.globalAvg < 6 OR ga.globalAvg IS NULL);

UPDATE Korisnik
SET brojNagrada = brojNagrada + (SELECT COUNT(*) FROM #FilmZaNagradu)
WHERE idKor = @idKor;

SELECT * FROM #FilmZaNagradu;

DROP TABLE #FilmZaNagradu;

END
GO

-- EXEC SP_REWARD_USER_PROC 2;

-- SELECT idKor, ime, brojNagrada FROM Korisnik WHERE idKor = 2;


CREATE OR ALTER FUNCTION F_USER_DESC 
(
    @id int
)
RETURNS VARCHAR(11)
AS
BEGIN
    declare @brojFilmova int

    SELECT @brojFilmova = COUNT(*)
    FROM Ocena
    WHERE idKor = @id

    if(@brojFilmova < 10)
        return 'undefined'

    declare @brojOznaka int

    SELECT @brojOznaka = COUNT(distinct oz.idOznaka)
    FROM Ocena o 
    LEFT JOIN FilmOznakaLink f ON (o.idFilm=f.idFilm)
    LEFT JOIN Oznaka oz ON(f.idOznaka=oz.idOznaka)
    WHERE o.idKor = @id AND oz.idOznaka IS NOT NULL

    if(@brojOznaka >= 10)
        return 'curious'

    return 'focused'
END
GO

CREATE OR ALTER FUNCTION F_USER_SPEC
(
    @id INT
)
RETURNS TABLE
AS
RETURN
(
    SELECT oz.oznaka
    FROM Ocena o
    JOIN FilmOznakaLink fz ON o.idFilm = fz.idFilm
    JOIN Oznaka oz ON fz.idOznaka = oz.idOznaka
    WHERE o.idKor = @id
      AND o.ocena >= 8
    GROUP BY oz.idOznaka, oz.oznaka
    HAVING COUNT(*) >= 2
);
GO

-- SELECT * FROM dbo.F_USER_SPEC(4);