package rs.ac.bg.etf.sab.pf220192;

import rs.ac.bg.etf.sab.connection.DB;
import rs.ac.bg.etf.sab.operations.GenresOperations;
import rs.ac.bg.etf.sab.util.DBUtil;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class pf220192_GenresOperations implements GenresOperations {

    @Override
    public Integer addGenre(String name) {
        if (doesGenreExist(name)) {
            return null;
        }
        return DBUtil.insertRecord("Zanr", "naziv",name);
    }


    @Override
    public Integer updateGenre(Integer idGenre, String name) {
        if (!DBUtil.recordExists("Zanr", idGenre)) {
            return null;
        }

        String sqlCheckName = "SELECT idZanr FROM Zanr WHERE naziv = ? AND idZanr != ?";
        try (var checkNameStatement = DB.getConnection().prepareStatement(sqlCheckName)) {
            checkNameStatement.setString(1, name);
            checkNameStatement.setInt(2, idGenre);
            var nameResult = checkNameStatement.executeQuery();
            if (nameResult.next()) {
                return -2;
            }
        } catch (SQLException e) {
            System.err.println("SQL Error checking name: " + e.getMessage());
            return -1;
        }

        boolean updated = DBUtil.updateSingleValue("Zanr", "naziv", name, idGenre);
        return updated ? idGenre : -1;
    }

    @Override
    public Integer removeGenre(Integer idGenre) {
        if (!DBUtil.recordExists("Zanr", idGenre)) {
            return null;
        }
        boolean deleted = DBUtil.deleteRecord("Zanr", idGenre);
        return deleted ? idGenre : -1;
    }


    @Override
    public boolean doesGenreExist(String name) {
        return DBUtil.valueExists("Zanr", "naziv", name);
    }

    @Override
    public Integer getGenreId(String name) {
        return DBUtil.getIdByValue("Zanr", "naziv", name);
    }

    @Override
    public List<Integer> getAllGenreIds() {
        return DBUtil.getAllIds("Zanr");
    }

}
