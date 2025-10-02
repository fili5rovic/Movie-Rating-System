package rs.ac.bg.etf.sab.pf220192;

import rs.ac.bg.etf.sab.operations.MoviesOperations;
import rs.ac.bg.etf.sab.util.DBUtil;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class pf220192_MoviesOperations implements MoviesOperations {
    @Override
    public Integer addMovie(String title, Integer genreId, String director) {
        Map<String, Object> values = new LinkedHashMap<>();
        values.put("naslov", title);
        values.put("reziser", director);

        Integer movieId = DBUtil.insertRecord("Film", values);
        if (movieId == -1) return null;

        if (genreId != null && DBUtil.recordExists("Zanr", genreId)) {
            Map<String, Object> linkValues = new LinkedHashMap<>();
            linkValues.put("idFilm", movieId);
            linkValues.put("idZanr", genreId);
            DBUtil.insertRecord("FilmZanrLink", linkValues);
        }

        return movieId;
    }

    @Override
    public Integer updateMovieTitle(Integer id, String newTitle) {
        if (!DBUtil.recordExists("Film", id))
            return null;

        DBUtil.updateSingleValue("Film", "naslov", newTitle, id);
        return id;
    }


    // ovde moram da vidim ako je vec linkovano
    @Override
    public Integer addGenreToMovie(Integer movieID, Integer genreID) {
        Map<String, Object> values = new LinkedHashMap<>();
        values.put("idFilm", movieID);
        values.put("idZanr", genreID);

        return DBUtil.insertRecord("FilmZanrLink", values);
    }

    @Override
    public Integer removeGenreFromMovie(Integer movieID, Integer genreID) {
        Integer linkID = DBUtil.getIdByValue("FilmZanrLink", "idFilm", movieID);
        if (linkID == null)
            return null;

        return DBUtil.deleteRecord("FilmZanrLink", linkID) ? linkID : null;
    }

    @Override
    public Integer updateMovieDirector(Integer id, String newDirector) {
        if (!DBUtil.recordExists("Film", id)) return null;
        return DBUtil.updateSingleValue("Film", "reziser", newDirector, id) ? id : null;
    }


    @Override
    public Integer removeMovie(Integer movieID) {
        return DBUtil.deleteRecord("Film", movieID) ? movieID : null;
    }

    @Override
    public List<Integer> getMovieIds(String s, String s1) {
        return List.of();
    }

    @Override
    public List<Integer> getAllMovieIds() {
        return DBUtil.getAllIds("Film");
    }

    @Override
    public List<Integer> getMovieIdsByGenre(Integer integer) {
        return List.of();
    }

    @Override
    public List<Integer> getGenreIdsForMovie(Integer integer) {
        return List.of();
    }

    @Override
    public List<Integer> getMovieIdsByDirector(String s) {
        return List.of();
    }
}
