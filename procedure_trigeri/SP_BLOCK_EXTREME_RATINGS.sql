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
