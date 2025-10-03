package rs.ac.bg.etf.sab.pf220192;

import rs.ac.bg.etf.sab.operations.UsersOperations;
import rs.ac.bg.etf.sab.util.Util;

import java.util.List;
import java.util.Map;

public class pf220192_UsersOperations implements UsersOperations {
    @Override
    public Integer addUser(String username) {
        return Util.insert("Korisnik", Map.of("ime",username,"brojNagrada",0));
    }

    @Override
    public Integer updateUser(Integer id, String newUsername) {
        return Util.updateById("Korisnik",id,Map.of("ime",newUsername)) ? id : null;
    }

    @Override
    public Integer removeUser(Integer id) {
        return Util.deleteById("Korisnik",id) ? id : null;
    }

    @Override
    public boolean doesUserExist(String username) {
        return !Util.fetchIdsWhere("Korisnik","ime=?",List.of(username)).isEmpty();
    }

    @Override
    public Integer getUserId(String username) {
        List<Integer> integers = Util.fetchColumnWhere("Korisnik", "ime", "ime=?", List.of(username));
        if(integers.isEmpty())
            return null;
        return integers.get(0);
    }

    @Override
    public List<Integer> getAllUserIds() {
        return Util.fetchAllIds("Korisnik");
    }

    @Override
    public List<Integer> getRecommendedMoviesFromFavoriteGenres(Integer id) {
        return List.of();
    }

    @Override
    public Integer getRewards(Integer id) {
        return 0;
    }

    @Override
    public List<String> getThematicSpecializations(Integer id) {
        return List.of();
    }

    @Override
    public String getUserDescription(Integer id) {
        return "";
    }
}
