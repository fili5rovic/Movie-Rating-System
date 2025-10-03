package rs.ac.bg.etf.sab.pf220192;

import rs.ac.bg.etf.sab.operations.RatingsOperations;
import rs.ac.bg.etf.sab.util.DBUtil;

import java.util.List;
import java.util.Map;

public class pf220192_RatingsOperations implements RatingsOperations {

    @Override
    public boolean addRating(Integer userId, Integer movieId, Integer score) {
        boolean exists = !DBUtil.fetchIdsWhere("Ocena", "idKor=? AND idFilm=?", List.of(userId, movieId)).isEmpty();
        if (exists) return false;

        boolean userExists = DBUtil.existsById("Korisnik", userId);
        boolean movieExists = DBUtil.existsById("Film", movieId);
        if (!userExists || !movieExists) return false;

        if (score == null || score < 1 || score > 10) return false;

        Integer idOcena = DBUtil.insert("Ocena", Map.of(
                "idKor", userId,
                "idFilm", movieId,
                "ocena", score
        ));
        return idOcena != null;
    }

    @Override
    public boolean updateRating(Integer userId, Integer movieId, Integer newScore) {
        List<Integer> ids = DBUtil.fetchIdsWhere("Ocena", "idKor=? AND idFilm=?", List.of(userId, movieId));
        if (ids.isEmpty()) return false;

        if (newScore == null || newScore < 1 || newScore > 10) return false;

        return DBUtil.updateById("Ocena", ids.get(0), Map.of("ocena", newScore));
    }

    @Override
    public boolean removeRating(Integer userId, Integer movieId) {
        List<Integer> ids = DBUtil.fetchIdsWhere("Ocena", "idKor=? AND idFilm=?", List.of(userId, movieId));
        if (ids.isEmpty()) return false;

        return DBUtil.deleteById("Ocena", ids.get(0));
    }

    @Override
    public Integer getRating(Integer userId, Integer movieId) {
        var ocene = DBUtil.fetchIdsWhere("Ocena", "idKor=? AND idFilm=?", List.of(userId, movieId));
        return ocene.get(0);
    }

    @Override
    public List<Integer> getRatedMoviesByUser(Integer userId) {
        return DBUtil.fetchColumnWhere("Ocena", "idFilm", "idKor=?", List.of(userId));
    }

    @Override
    public List<Integer> getUsersWhoRatedMovie(Integer movieId) {
        return DBUtil.fetchColumnWhere("Ocena", "idKor", "idFilm=?", List.of(movieId));
    }
}