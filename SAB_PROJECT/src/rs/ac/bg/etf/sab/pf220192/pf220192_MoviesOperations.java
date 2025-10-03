package rs.ac.bg.etf.sab.pf220192;

import rs.ac.bg.etf.sab.operations.MoviesOperations;
import rs.ac.bg.etf.sab.util.Util;

import java.util.List;
import java.util.Map;

public class pf220192_MoviesOperations implements MoviesOperations {

    @Override
    public Integer addMovie(String title, Integer genreId, String director) {
        Integer movieId = Util.insert("Film", Map.of("naslov", title, "reziser", director));
        if (movieId == null)
            return null;

        Util.insert("FilmZanrLink", Map.of("idFilm", movieId, "idZanr", genreId));
        return movieId;
    }

    @Override
    public Integer updateMovieTitle(Integer movieId, String title) {
        return Util.updateById("Film", movieId, Map.of("naslov", title)) ? movieId : null;
    }

    @Override
    public Integer addGenreToMovie(Integer movieId, Integer genreId) {
        return Util.insert("FilmZanrLink", Map.of("idFilm", movieId, "idZanr", genreId));
    }

    @Override
    public Integer removeGenreFromMovie(Integer movieId, Integer genreId) {
        return Util.deleteWhere("FilmZanrLink", "idFilm=? and idZanr=?", List.of(movieId, genreId)) ? movieId : null;
    }

    @Override
    public Integer updateMovieDirector(Integer id, String director) {
        return Util.updateById("Film", id, Map.of("reziser", director)) ? id : null;
    }

    @Override
    public Integer removeMovie(Integer id) {
        Util.deleteWhere("FilmZanrLink", "idFilm = ?", List.of(id));
        Util.deleteWhere("FilmOznakaLink", "idFilm = ?", List.of(id));
        Util.deleteWhere("Lista", "idFilm = ?", List.of(id));
        Util.deleteWhere("Ocena", "idFilm = ?", List.of(id));
        return Util.deleteById("Film", id) ? id : null;
    }


    @Override
    public List<Integer> getMovieIds(String title, String director) {
        return Util.fetchIdsWhere("Film", "naslov=? and reziser=?", List.of(title, director));
    }

    @Override
    public List<Integer> getAllMovieIds() {
        return Util.fetchAllIds("Film");
    }

    public List<Integer> getMovieIdsByGenre(Integer genreId) {
        return Util.fetchColumnWhere("FilmZanrLink", "idFilm", "idZanr=?", List.of(genreId));
    }

    public List<Integer> getGenreIdsForMovie(Integer movieId) {
        return Util.fetchColumnWhere("FilmZanrLink", "idZanr", "idFilm=?", List.of(movieId));
    }

    @Override
    public List<Integer> getMovieIdsByDirector(String director) {
        return Util.fetchIdsWhere("Film", "reziser=?", List.of(director));
    }
}
