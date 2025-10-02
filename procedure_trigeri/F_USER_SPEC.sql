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

SELECT * FROM dbo.F_USER_SPEC(4);