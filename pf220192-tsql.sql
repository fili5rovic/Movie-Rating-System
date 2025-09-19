CREATE OR ALTER FUNCTION F_USER_DESC
(
    @idKor INT
)
RETURNS VARCHAR(20)
AS
BEGIN
    DECLARE @brojOcena INT;
    DECLARE @brojRazlicitihTagova INT;

    SELECT @brojOcena = COUNT(*) 
    FROM Ocena 
    WHERE idKor = @idKor;

     IF @brojOcena < 10
        RETURN 'nedefinisan';

    SELECT @brojRazlicitihTagova = COUNT(DISTINCT fol.idOznaka)
    FROM Ocena o
    JOIN FilmOznakaLink fol ON o.idFilm = fol.idFilm
    WHERE o.idKor = @idKor;

    IF @brojRazlicitihTagova < 10
        RETURN 'fokusiran';
 

    RETURN 'radoznao';
END
GO

SELECT 
    k.idKor,
    k.ime,
    dbo.F_USER_DESC(k.idKor) AS OpisKorisnika,
    (SELECT COUNT(*) FROM Ocena WHERE idKor = k.idKor) AS BrojOcena,
    (SELECT COUNT(DISTINCT fol.idOznaka)
     FROM Ocena o
     JOIN FilmOznakaLink fol ON o.idFilm = fol.idFilm
     WHERE o.idKor = k.idKor) AS BrojTagova
FROM Korisnik k;