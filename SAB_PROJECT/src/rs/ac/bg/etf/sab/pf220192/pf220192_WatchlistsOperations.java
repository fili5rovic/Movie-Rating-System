package rs.ac.bg.etf.sab.pf220192;

import rs.ac.bg.etf.sab.operations.WatchlistsOperations;
import rs.ac.bg.etf.sab.util.Util;

import java.util.List;
import java.util.Map;

public class pf220192_WatchlistsOperations implements WatchlistsOperations {
    @Override
    public boolean addMovieToWatchlist(Integer userId, Integer movieId) {
        if(Util.existsWhere("Lista","idKor=? and idFilm=?",List.of(userId,movieId)))
            return false;

        return Util.insert("Lista", Map.of("idKor", userId, "idFilm", movieId)) != null;
    }

    @Override
    public boolean removeMovieFromWatchlist(Integer userId, Integer movieId) {
        return Util.deleteWhere("Lista","idKor=? and idFilm=?",List.of(userId,movieId));
    }

    @Override
    public boolean isMovieInWatchlist(Integer userId, Integer movieId) {
        return Util.existsWhere("Lista","idKor=? and idFilm=?", List.of(userId,movieId));
    }

    @Override
    public List<Integer> getMoviesInWatchlist(Integer userId) {
        return Util.fetchColumnWhere("Lista","idFilm","idKor=?",List.of(userId));
    }

    @Override
    public List<Integer> getUsersWithMovieInWatchlist(Integer movieId) {
        return Util.fetchColumnWhere("Lista","idKor","idFilm=?",List.of(movieId));
    }
}
