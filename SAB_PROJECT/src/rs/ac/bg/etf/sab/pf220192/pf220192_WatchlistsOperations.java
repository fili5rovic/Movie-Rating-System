package rs.ac.bg.etf.sab.pf220192;

import rs.ac.bg.etf.sab.operations.WatchlistsOperations;

import java.util.List;

public class pf220192_WatchlistsOperations implements WatchlistsOperations {
    @Override
    public boolean addMovieToWatchlist(Integer integer, Integer integer1) {
        return false;
    }

    @Override
    public boolean removeMovieFromWatchlist(Integer integer, Integer integer1) {
        return false;
    }

    @Override
    public boolean isMovieInWatchlist(Integer integer, Integer integer1) {
        return false;
    }

    @Override
    public List<Integer> getMoviesInWatchlist(Integer integer) {
        return List.of();
    }

    @Override
    public List<Integer> getUsersWithMovieInWatchlist(Integer integer) {
        return List.of();
    }
}
