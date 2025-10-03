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