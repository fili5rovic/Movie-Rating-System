package student;

import rs.ac.bg.etf.sab.operations.GenresOperations;
import student.util.DBUtil;

import java.util.List;
import java.util.Map;


public class pf220192_GenresOperations implements GenresOperations {


    @Override
    public Integer addGenre(String name) {
        if(DBUtil.existsWhere("Zanr","naziv=?",List.of(name)))
            return null;
        return DBUtil.insert("Zanr", Map.of("naziv", name));
    }

    @Override
    public Integer updateGenre(Integer id, String name) {
        return DBUtil.updateById("Zanr", id, Map.of("naziv", name)) ? id : null;
    }

    @Override
    public Integer removeGenre(Integer id) {
        return DBUtil.deleteById("Zanr", id) ? id : null;
    }

    @Override
    public boolean doesGenreExist(String name) {
        return getGenreId(name) != null;
    }

    @Override
    public Integer getGenreId(String name) {
        List<Integer> ids = DBUtil.fetchIdsWhere("Zanr","naziv=?",List.of(name));
        return ids.isEmpty() ? null : ids.get(0);
    }

    @Override
    public List<Integer> getAllGenreIds() {
        return DBUtil.fetchAllIds("Zanr");
    }
}
