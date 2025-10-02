package rs.ac.bg.etf.sab.pf220192;

import rs.ac.bg.etf.sab.operations.MoviesOperations;

import java.util.List;

public class pf220192_MoviesOperations implements MoviesOperations {
    @Override
    public Integer addMovie(String s, Integer integer, String s1) {
        return 0;
    }

    @Override
    public Integer updateMovieTitle(Integer integer, String s) {
        return 0;
    }

    @Override
    public Integer addGenreToMovie(Integer integer, Integer integer1) {
        return 0;
    }

    @Override
    public Integer removeGenreFromMovie(Integer integer, Integer integer1) {
        return 0;
    }

    @Override
    public Integer updateMovieDirector(Integer integer, String s) {
        return 0;
    }

    @Override
    public Integer removeMovie(Integer integer) {
        return 0;
    }

    @Override
    public List<Integer> getMovieIds(String s, String s1) {
        return List.of();
    }

    @Override
    public List<Integer> getAllMovieIds() {
        return List.of();
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
