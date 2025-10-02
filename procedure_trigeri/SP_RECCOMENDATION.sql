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

EXEC SP_RECOMMENDATION 1;
