package rs.ac.bg.etf.sab.pf220192;

import rs.ac.bg.etf.sab.operations.MoviesOperations;
import rs.ac.bg.etf.sab.util.DBUtil;

import java.util.List;
import java.util.Map;

public class pf220192_MoviesOperations implements MoviesOperations {

    @Override
    public Integer addMovie(String title, Integer genreId, String director) {
        Integer movieId = DBUtil.insert("Film", Map.of("naslov", title, "reziser", director));
        if (movieId == null)
            return null;

        DBUtil.insert("FilmZanrLink", Map.of("idFilm", movieId, "idZanr", genreId));
        return movieId;
    }

    @Override
    public Integer updateMovieTitle(Integer movieId, String title) {
        return DBUtil.updateById("Film", movieId, Map.of("naslov", title)) ? movieId : null;
    }

    @Override
    public Integer addGenreToMovie(Integer movieId, Integer genreId) {
        if (DBUtil.fetchIdsWhere("Film", "idFilm=?", List.of(movieId)).isEmpty()) {
            return null;
        }

        if (!DBUtil.fetchColumnWhere(
                "FilmZanrLink", "idFilm", "idFilm=? and idZanr=?", List.of(movieId, genreId)).isEmpty()) {
            return null;
        }

        Integer linkId = DBUtil.insert("FilmZanrLink", Map.of("idFilm", movieId, "idZanr", genreId));
        return linkId != null ? movieId : null;
    }


    @Override
    public Integer removeGenreFromMovie(Integer movieId, Integer genreId) {
        return DBUtil.deleteWhere("FilmZanrLink", "idFilm=? and idZanr=?", List.of(movieId, genreId)) ? movieId : null;
    }

    @Override
    public Integer updateMovieDirector(Integer id, String director) {
        return DBUtil.updateById("Film", id, Map.of("reziser", director)) ? id : null;
    }

    @Override
    public Integer removeMovie(Integer id) {
        DBUtil.deleteWhere("FilmZanrLink", "idFilm = ?", List.of(id));
        DBUtil.deleteWhere("FilmOznakaLink", "idFilm = ?", List.of(id));
        DBUtil.deleteWhere("Lista", "idFilm = ?", List.of(id));
        DBUtil.deleteWhere("Ocena", "idFilm = ?", List.of(id));
        return DBUtil.deleteById("Film", id) ? id : null;
    }


    @Override
    public List<Integer> getMovieIds(String title, String director) {
        return DBUtil.fetchIdsWhere("Film", "naslov=? and reziser=?", List.of(title, director));
    }

    @Override
    public List<Integer> getAllMovieIds() {
        return DBUtil.fetchAllIds("Film");
    }

    public List<Integer> getMovieIdsByGenre(Integer genreId) {
        return DBUtil.fetchColumnWhere("FilmZanrLink", "idFilm", "idZanr=?", List.of(genreId));
    }

    public List<Integer> getGenreIdsForMovie(Integer movieId) {
        return DBUtil.fetchColumnWhere("FilmZanrLink", "idZanr", "idFilm=?", List.of(movieId));
    }

    @Override
    public List<Integer> getMovieIdsByDirector(String director) {
        return DBUtil.fetchIdsWhere("Film", "reziser=?", List.of(director));
    }
}
