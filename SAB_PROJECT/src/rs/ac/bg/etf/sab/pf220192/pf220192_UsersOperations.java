package rs.ac.bg.etf.sab.pf220192;

import rs.ac.bg.etf.sab.operations.UsersOperations;

import java.util.List;

public class pf220192_UsersOperations implements UsersOperations {
    @Override
    public Integer addUser(String s) {
        return 0;
    }

    @Override
    public Integer updateUser(Integer integer, String s) {
        return 0;
    }

    @Override
    public Integer removeUser(Integer integer) {
        return 0;
    }

    @Override
    public boolean doesUserExist(String s) {
        return false;
    }

    @Override
    public Integer getUserId(String s) {
        return 0;
    }

    @Override
    public List<Integer> getAllUserIds() {
        return List.of();
    }

    @Override
    public List<Integer> getRecommendedMoviesFromFavoriteGenres(Integer integer) {
        return List.of();
    }

    @Override
    public Integer getRewards(Integer integer) {
        return 0;
    }

    @Override
    public List<String> getThematicSpecializations(Integer integer) {
        return List.of();
    }

    @Override
    public String getUserDescription(Integer integer) {
        return "";
    }
}
