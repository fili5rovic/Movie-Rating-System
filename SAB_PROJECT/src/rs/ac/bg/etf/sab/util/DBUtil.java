package rs.ac.bg.etf.sab.util;

import rs.ac.bg.etf.sab.connection.DB;

import java.sql.SQLException;

public class DBUtil {

    public static boolean recordExists(String tableName, String idColumn, Integer id) {
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

    public static Integer getIdByValue(String tableName, String idColumn, String searchColumn, String value) {
        String sql = String.format("SELECT %s FROM %s WHERE %s = ?", idColumn, tableName, searchColumn);

        try (var statement = DB.getConnection().prepareStatement(sql)) {
            statement.setString(1, value);
            var resultSet = statement.executeQuery();
            return resultSet.next() ? resultSet.getInt(1) : null;

        } catch (SQLException e) {
            System.err.println("SQL Error in getIdByValue: " + e.getMessage());
            return null;
        }
    }

    public static boolean deleteRecord(String tableName, String idColumn, Integer id) {
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