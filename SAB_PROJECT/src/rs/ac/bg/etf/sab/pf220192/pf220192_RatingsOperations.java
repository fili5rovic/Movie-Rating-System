package rs.ac.bg.etf.sab.pf220192;

import rs.ac.bg.etf.sab.operations.RatingsOperations;

import java.util.List;

public class pf220192_RatingsOperations implements RatingsOperations {
    @Override
    public boolean addRating(Integer integer, Integer integer1, Integer integer2) {
        return false;
    }

    @Override
    public boolean updateRating(Integer integer, Integer integer1, Integer integer2) {
        return false;
    }

    @Override
    public boolean removeRating(Integer integer, Integer integer1) {
        return false;
    }

    @Override
    public Integer getRating(Integer integer, Integer integer1) {
        return 0;
    }

    @Override
    public List<Integer> getRatedMoviesByUser(Integer integer) {
        return List.of();
    }

    @Override
    public List<Integer> getUsersWhoRatedMovie(Integer integer) {
        return List.of();
    }
}
