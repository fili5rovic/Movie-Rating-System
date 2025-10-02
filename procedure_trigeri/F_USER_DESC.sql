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
		return 'nedefinisan'

	declare @brojOznaka int

	SELECT @brojOznaka = COUNT(distinct oz.idOznaka)
	FROM Ocena o JOIN FilmOznakaLink f ON (o.idFilm=f.idFilm) JOIN Oznaka oz ON(f.idOznaka=oz.idOznaka)
	WHERE idKor = @id

	if(@brojOznaka>=5)
		return 'radoznao'

	return 'fokusiran'

END
GO

SELECT 
    k.idKor, 
    dbo.F_USER_DESC(k.idKor) AS opis,
    (SELECT COUNT(DISTINCT f.idOznaka)
     FROM Ocena o
     JOIN FilmOznakaLink f ON o.idFilm = f.idFilm
     WHERE o.idKor = k.idKor) AS ukupno
FROM Korisnik k;