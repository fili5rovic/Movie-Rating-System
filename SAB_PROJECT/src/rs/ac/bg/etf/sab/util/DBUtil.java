package rs.ac.bg.etf.sab.util;

import rs.ac.bg.etf.sab.connection.DB;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DBUtil {

    private static final HashMap<String, String> tableIdPairs = new HashMap<>();

    static {
        tableIdPairs.put("Korisnik", "idKor");
        tableIdPairs.put("Film", "idFilm");
        tableIdPairs.put("Oznaka", "idOznaka");
        tableIdPairs.put("Zanr", "idZanr");
        tableIdPairs.put("Ocena", "idOcena");
        tableIdPairs.put("Lista", "idLista");
        tableIdPairs.put("FilmOznakaLink", "idLink");
        tableIdPairs.put("FilmZanrLink", "idLink");
    }

    private static String getIdColumn(String tableName) {
        String idColumn = tableIdPairs.get(tableName);
        if (idColumn == null) {
            throw new IllegalArgumentException("No id mapping for table: " + tableName);
        }
        return idColumn;
    }

    public static boolean recordExists(String tableName, Integer id) {
        String idColumn = getIdColumn(tableName);
        String sql = String.format("SELECT 1 FROM %s WHERE %s = ?", tableName, idColumn);

        try (var statement = DB.getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);
            var resultSet = statement.executeQuery();
            return resultSet.next();

        } catch (SQLException e) {
            System.err.println("SQL Error in recordExists: " + e.getMessage());
            return false;
        }
    }

    public static boolean valueExists(String tableName, String columnName, String value) {
        String sql = String.format("SELECT 1 FROM %s WHERE %s = ?", tableName, columnName);

        try (var statement = DB.getConnection().prepareStatement(sql)) {
            statement.setString(1, value);
            var resultSet = statement.executeQuery();
            return resultSet.next();

        } catch (SQLException e) {
            System.err.println("SQL Error in valueExists: " + e.getMessage());
            return false;
        }
    }

    public static Integer getIdByValue(String tableName, String searchColumn, Object value) {
        String idColumn = getIdColumn(tableName);
        String sql = String.format("SELECT %s FROM %s WHERE %s = ?", idColumn, tableName, searchColumn);

        try (var statement = DB.getConnection().prepareStatement(sql)) {
            statement.setObject(1, value);
            var resultSet = statement.executeQuery();
            return resultSet.next() ? resultSet.getInt(1) : null;

        } catch (SQLException e) {
            System.err.println("SQL Error in getIdByValue: " + e.getMessage());
            return null;
        }
    }

    public static boolean updateMultipleValues(String tableName,
                                               Map<String, Object> updates,
                                               Integer id) {
        if (updates == null || updates.isEmpty()) {
            return false;
        }

        String idColumn = getIdColumn(tableName);

        String setClause = updates.keySet().stream()
                .map(col -> col + " = ?")
                .collect(Collectors.joining(", "));

        String sql = String.format("UPDATE %s SET %s WHERE %s = ?",
                tableName, setClause, idColumn);

        try (var statement = DB.getConnection().prepareStatement(sql)) {
            int index = 1;

            for (var value : updates.values()) {
                statement.setObject(index++, value);
            }

            statement.setInt(index, id);

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            System.err.println("SQL Error in updateMultipleValues: " + e.getMessage());
            return false;
        }
    }

    public static Integer insertRecord(String tableName, String column, Object val) {
        Map<String, Object> map = new HashMap<>();
        map.put(column,val);

        return insertRecord(tableName, map);
    }

    public static Integer insertRecord(String tableName, Map<String, Object> values) {
        if (values == null || values.isEmpty()) {
            throw new IllegalArgumentException("Values map cannot be null or empty.");
        }

        String columns = String.join(", ", values.keySet());
        String placeholders = values.keySet().stream()
                .map(k -> "?")
                .collect(Collectors.joining(", "));

        String sql = String.format("INSERT INTO %s(%s) VALUES(%s)", tableName, columns, placeholders);

        try (var statement = DB.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            int index = 1;
            for (Object value : values.values()) {
                statement.setObject(index++, value);
            }

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 1) {
                var generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.err.println("SQL Error in insertRecord: " + e.getMessage());
            e.printStackTrace();
        }

        return -1;
    }


    public static boolean updateSingleValue(String tableName, String columnName, Object value, Integer id) {
        String idColumn = getIdColumn(tableName);
        String sql = String.format("UPDATE %s SET %s = ? WHERE %s = ?", tableName, columnName, idColumn);

        try (var statement = DB.getConnection().prepareStatement(sql)) {
            statement.setObject(1, value);
            statement.setInt(2, id);
            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            System.err.println("SQL Error in updateSingleValue: " + e.getMessage());
            return false;
        }
    }

    public static List<Integer> getAllIds(String tableName) {
        String idColumn = getIdColumn(tableName);
        List<Integer> ids = new ArrayList<>();
        String sql = String.format("SELECT %s FROM %s ORDER BY %s", idColumn, tableName, idColumn);

        try (var statement = DB.getConnection().createStatement();
             var resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                ids.add(resultSet.getInt(1));
            }

        } catch (SQLException e) {
            System.err.println("SQL Error in getAllIds: " + e.getMessage());
        }

        return ids;
    }

    public static boolean deleteRecord(String tableName, Integer id) {
        String idColumn = getIdColumn(tableName);
        String sql = String.format("DELETE FROM %s WHERE %s = ?", tableName, idColumn);

        try (var statement = DB.getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            System.err.println("SQL Error in deleteRecord: " + e.getMessage());
            return false;
        }
    }
}