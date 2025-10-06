package student;

import rs.ac.bg.etf.sab.operations.WatchlistsOperations;
import student.util.DBUtil;

import java.util.List;
import java.util.Map;

public class pf220192_WatchlistsOperations implements WatchlistsOperations {
    @Override
    public boolean addMovieToWatchlist(Integer userId, Integer movieId) {
        if(DBUtil.existsWhere("Lista","idKor=? and idFilm=?",List.of(userId,movieId)))
            return false;

        return DBUtil.insert("Lista", Map.of("idKor", userId, "idFilm", movieId)) != null;
    }

    @Override
    public boolean removeMovieFromWatchlist(Integer userId, Integer movieId) {
        return DBUtil.deleteWhere("Lista","idKor=? and idFilm=?",List.of(userId,movieId));
    }

    @Override
    public boolean isMovieInWatchlist(Integer userId, Integer movieId) {
        return DBUtil.existsWhere("Lista","idKor=? and idFilm=?", List.of(userId,movieId));
    }

    @Override
    public List<Integer> getMoviesInWatchlist(Integer userId) {
        return DBUtil.fetchColumnWhere("Lista","idFilm","idKor=?",List.of(userId));
    }

    @Override
    public List<Integer> getUsersWithMovieInWatchlist(Integer movieId) {
        return DBUtil.fetchColumnWhere("Lista","idKor","idFilm=?",List.of(movieId));
    }
}
