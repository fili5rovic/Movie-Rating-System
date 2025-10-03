package rs.ac.bg.etf.sab.pf220192;

import rs.ac.bg.etf.sab.operations.RatingsOperations;
import rs.ac.bg.etf.sab.util.Util;

import java.util.List;
import java.util.Map;

public class pf220192_RatingsOperations implements RatingsOperations {

    @Override
    public boolean addRating(Integer userId, Integer movieId, Integer score) {
        boolean exists = !Util.fetchIdsWhere("Ocena", "idKor=? AND idFilm=?", List.of(userId, movieId)).isEmpty();
        if (exists) return false;

        boolean userExists = Util.existsById("Korisnik", userId);
        boolean movieExists = Util.existsById("Film", movieId);
        if (!userExists || !movieExists) return false;

        if (score == null || score < 1 || score > 10) return false;

        Integer idOcena = Util.insert("Ocena", Map.of(
                "idKor", userId,
                "idFilm", movieId,
                "ocena", score
        ));
        return idOcena != null;
    }

    @Override
    public boolean updateRating(Integer userId, Integer movieId, Integer newScore) {
        List<Integer> ids = Util.fetchIdsWhere("Ocena", "idKor=? AND idFilm=?", List.of(userId, movieId));
        if (ids.isEmpty()) return false;

        if (newScore == null || newScore < 1 || newScore > 10) return false;

        return Util.updateById("Ocena", ids.get(0), Map.of("ocena", newScore));
    }

    @Override
    public boolean removeRating(Integer userId, Integer movieId) {
        List<Integer> ids = Util.fetchIdsWhere("Ocena", "idKor=? AND idFilm=?", List.of(userId, movieId));
        if (ids.isEmpty()) return false;

        return Util.deleteById("Ocena", ids.get(0));
    }

    @Override
    public Integer getRating(Integer userId, Integer movieId) {
        List<Integer> ocene = Util.fetchColumnWhere("Ocena", "ocena", "idKor=? AND idFilm=?", List.of(userId, movieId));
        if (ocene.isEmpty()) return null;
        return ocene.get(0);
    }


    @Override
    public List<Integer> getRatedMoviesByUser(Integer userId) {
        return Util.fetchColumnWhere("Ocena", "idFilm", "idKor=?", List.of(userId));
    }

    @Override
    public List<Integer> getUsersWhoRatedMovie(Integer movieId) {
        return Util.fetchColumnWhere("Ocena", "idKor", "idFilm=?", List.of(movieId));
    }
}