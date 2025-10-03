package rs.ac.bg.etf.sab.util;

import rs.ac.bg.etf.sab.connection.DB;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class Util {

    // --- Mapping table names to their primary key column ---
    private static final Map<String, String> tableIdMap = Map.of(
            "Korisnik", "idKor",
            "Film", "idFilm",
            "Oznaka", "idOznaka",
            "Zanr", "idZanr",
            "Ocena", "idOcena",
            "Lista", "idLista",
            "FilmOznakaLink", "idLink",
            "FilmZanrLink", "idLink"
    );

    private static String getIdColumn(String table) {
        String idCol = tableIdMap.get(table);
        if (idCol == null)
            throw new IllegalArgumentException("Unknown table: " + table);
        return idCol;
    }

    // --- CREATE ---
    public static Integer insert(String table, Map<String, Object> values) {
        if (values == null || values.isEmpty())
            throw new IllegalArgumentException("Values map cannot be empty");

        String columns = String.join(", ", values.keySet());
        String placeholders = values.keySet().stream().map(k -> "?").collect(Collectors.joining(", "));
        String sql = String.format("INSERT INTO %s (%s) VALUES (%s)", table, columns, placeholders);

        try (PreparedStatement ps = DB.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setParams(ps, values.values());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in insert: " + e.getMessage());
        }
        return null;
    }

    // --- READ ---
    public static List<String> fetchColumns(String table, String column) {
        String sql = String.format("SELECT %s FROM %s", column, table);
        List<String> result = new ArrayList<>();
        try (PreparedStatement ps = DB.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) result.add(rs.getString(1));
        } catch (SQLException e) {
            System.err.println("SQL Error in fetchColumns: " + e.getMessage());
        }
        return result;
    }

    public static List<Integer> fetchIdsWhere(String table, String whereClause, List<Object> params) {
        String idCol = getIdColumn(table);
        String sql = String.format("SELECT %s FROM %s WHERE %s", idCol, table, whereClause);
        List<Integer> result = new ArrayList<>();
        try (PreparedStatement ps = DB.getConnection().prepareStatement(sql)) {
            if (params != null && !params.isEmpty() && whereClause.contains("?")) {
                setParams(ps, params);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in fetchIdsWhere: " + e.getMessage());
        }
        return result;
    }

    public static List<Integer> fetchColumnWhere(String table, String column, String whereClause, List<Object> params) {
        String sql = String.format("SELECT %s FROM %s WHERE %s", column, table, whereClause);
        List<Integer> result = new ArrayList<>();
        try (PreparedStatement ps = DB.getConnection().prepareStatement(sql)) {
            if (params != null && !params.isEmpty() && whereClause.contains("?")) {
                setParams(ps, params);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) result.add(rs.getInt(1));
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in fetchColumnWhere: " + e.getMessage());
        }
        return result;
    }

    public static List<Integer> fetchAllIds(String table) {
        String idCol = getIdColumn(table);
        String sql = String.format("SELECT %s FROM %s", idCol, table);
        List<Integer> result = new ArrayList<>();
        try (PreparedStatement ps = DB.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) result.add(rs.getInt(1));
        } catch (SQLException e) {
            System.err.println("SQL Error in fetchAllIds: " + e.getMessage());
        }
        return result;
    }

    public static boolean updateById(String table, Integer id, Map<String, Object> updates) {
        if (updates == null || updates.isEmpty())
            throw new IllegalArgumentException("Updates map cannot be empty");

        String idCol = getIdColumn(table);
        String setClause = updates.keySet().stream()
                .map(col -> col + " = ?")
                .collect(Collectors.joining(", "));
        String sql = String.format("UPDATE %s SET %s WHERE %s = ?", table, setClause, idCol);

        try (PreparedStatement ps = DB.getConnection().prepareStatement(sql)) {
            setParams(ps, updates.values());
            ps.setObject(updates.size() + 1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("SQL Error in updateById: " + e.getMessage());
        }
        return false;
    }

    // --- DELETE ---
    public static boolean deleteById(String table, Integer id) {
        String idCol = getIdColumn(table);
        String sql = String.format("DELETE FROM %s WHERE %s = ?", table, idCol);
        try (PreparedStatement ps = DB.getConnection().prepareStatement(sql)) {
            ps.setObject(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("SQL Error in deleteById: " + e.getMessage());
        }
        return false;
    }

    public static boolean deleteWhere(String table, String whereClause, List<Object> params) {
        String sql = String.format("DELETE FROM %s WHERE %s", table, whereClause);
        try (PreparedStatement ps = DB.getConnection().prepareStatement(sql)) {
            setParams(ps, params);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("SQL Error in deleteWhere: " + e.getMessage());
        }
        return false;
    }

    public static boolean existsById(String table, Integer id) {
        String idCol = getIdColumn(table);
        String sql = String.format("SELECT 1 FROM %s WHERE %s = ?", table, idCol);
        try (PreparedStatement ps = DB.getConnection().prepareStatement(sql)) {
            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in existsById: " + e.getMessage());
        }
        return false;
    }

    public static boolean existsWhere(String table, String whereClause, List<Object> params) {
        String sql = String.format("SELECT 1 FROM %s WHERE %s", table, whereClause);
        try (PreparedStatement ps = DB.getConnection().prepareStatement(sql)) {
            setParams(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in existsWhere: " + e.getMessage());
        }
        return false;
    }

    private static void setParams(PreparedStatement ps, Collection<?> values) throws SQLException {
        if (values == null) return;
        int i = 1;
        for (Object v : values) ps.setObject(i++, v);
    }


}