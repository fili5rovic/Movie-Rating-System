package student;

import rs.ac.bg.etf.sab.operations.UsersOperations;
import student.util.DBUtil;

import java.util.List;
import java.util.Map;

public class pf220192_UsersOperations implements UsersOperations {
    @Override
    public Integer addUser(String username) {
        if(DBUtil.existsWhere("Korisnik","ime=?",List.of(username)))
            return null;
        return DBUtil.insert("Korisnik", Map.of("ime",username,"brojNagrada",0));
    }

    @Override
    public Integer updateUser(Integer id, String newUsername) {
        return DBUtil.updateById("Korisnik",id,Map.of("ime",newUsername)) ? id : null;
    }

    @Override
    public Integer removeUser(Integer id) {
        return DBUtil.deleteById("Korisnik",id) ? id : null;
    }

    @Override
    public boolean doesUserExist(String username) {
        return !DBUtil.fetchIdsWhere("Korisnik","ime=?",List.of(username)).isEmpty();
    }

    @Override
    public Integer getUserId(String username) {
        List<Integer> integers = DBUtil.fetchColumnWhere("Korisnik", "idKor", "ime=?", List.of(username));
        if(integers.isEmpty())
            return null;
        return integers.get(0);
    }

    @Override
    public List<Integer> getAllUserIds() {
        return DBUtil.fetchAllIds("Korisnik");
    }

    @Override
    public List<Integer> getRecommendedMoviesFromFavoriteGenres(Integer id) {
        return DBUtil.callProcedureForIntColumn("SP_RECOMMENDATION", id, "idFilm");
    }

    @Override
    public Integer getRewards(Integer id) {
        DBUtil.callVoidProcedure("SP_REWARD_USER_PROC", id);

        List<Integer> nagrade = DBUtil.fetchColumnWhere("Korisnik", "brojNagrada", "idKor=?", List.of(id));
        if(nagrade.isEmpty())
            return 0;
        return nagrade.get(0);
    }

    @Override
    public List<String> getThematicSpecializations(Integer id) {
        return DBUtil.callTableFunction("F_USER_SPEC", id, "oznaka");
    }

    @Override
    public String getUserDescription(Integer id) {
        return DBUtil.callScalarFunction("F_USER_DESC",id);
    }
}
