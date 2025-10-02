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

EXEC SP_REWARD_USER_PROC 2;

SELECT idKor, ime, brojNagrada FROM Korisnik WHERE idKor = 2;
