package rs.ac.bg.etf.sab.pf220192;

import rs.ac.bg.etf.sab.connection.DB;
import rs.ac.bg.etf.sab.operations.GenresOperations;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class pf220192_GenresOperations implements GenresOperations {

    private boolean genreExists(Integer idGenre) {
        String sqlCheckExisting = "SELECT idZanr FROM Zanr WHERE idZanr = ?";

        try (var checkStatement = DB.getConnection().prepareStatement(sqlCheckExisting)) {
            checkStatement.setInt(1, idGenre);
            var resultSet = checkStatement.executeQuery();
            return resultSet.next();

        } catch (SQLException e) {
            System.err.println("SQL Error in genreExists: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Integer addGenre(String name) {
        if (doesGenreExist(name)) {
            return null;
        }

        String sqlInsert = "INSERT INTO Zanr(naziv) VALUES(?)";

        try (var insertStatement = DB.getConnection().prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
            insertStatement.setString(1, name);
            int affectedRows = insertStatement.executeUpdate();

            if (affectedRows == 1) {
                var generatedKeys = insertStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.err.println("SQL Error in addGenre: " + e.getMessage());
            e.printStackTrace();
        }

        return -1;
    }

    @Override
    public Integer updateGenre(Integer idGenre, String name) {
        if (!genreExists(idGenre)) {
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

        String sqlUpdate = "UPDATE Zanr SET naziv = ? WHERE idZanr = ?";
        try (var updateStatement = DB.getConnection().prepareStatement(sqlUpdate)) {
            updateStatement.setString(1, name);
            updateStatement.setInt(2, idGenre);
            int num = updateStatement.executeUpdate();
            return num == 1 ? idGenre : -1;

        } catch (SQLException e) {
            System.err.println("SQL Error in updateGenre: " + e.getMessage());
            return -1;
        }
    }

    @Override
    public Integer removeGenre(Integer idGenre) {
        if (!genreExists(idGenre)) {
            return null;
        }

        String sqlDelete = "DELETE FROM Zanr WHERE idZanr = ?";
        try (var deleteStatement = DB.getConnection().prepareStatement(sqlDelete)) {
            deleteStatement.setInt(1, idGenre);
            int num = deleteStatement.executeUpdate();
            return num == 1 ? 1 : -1;

        } catch (SQLException e) {
            System.err.println("SQL Error in removeGenre: " + e.getMessage());
            return -1;
        }
    }

    @Override
    public boolean doesGenreExist(String name) {
        String sqlCheckExisting = "SELECT idZanr FROM Zanr WHERE naziv = ?";

        try (var checkStatement = DB.getConnection().prepareStatement(sqlCheckExisting)) {
            checkStatement.setString(1, name);
            var resultSet = checkStatement.executeQuery();
            return resultSet.next();

        } catch (SQLException e) {
            System.err.println("SQL Error in genreNameExists: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Integer getGenreId(String name) {
        String sql = "SELECT idZanr FROM Zanr WHERE naziv = ?";
        try (var statement = DB.getConnection().prepareStatement(sql)) {
            statement.setString(1, name);
            var resultSet = statement.executeQuery();
            return resultSet.next() ? resultSet.getInt(1) : null;

        } catch (SQLException e) {
            System.err.println("SQL Error in getGenreId: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Integer> getAllGenreIds() {
        List<Integer> ids = new ArrayList<>();
        String sql = "SELECT idZanr FROM Zanr ORDER BY idZanr";

        try (var statement = DB.getConnection().createStatement();
             var resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                ids.add(resultSet.getInt(1));
            }

        } catch (SQLException e) {
            System.err.println("SQL Error in getAllGenreIds: " + e.getMessage());
        }

        return ids;
    }
}
