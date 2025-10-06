package student;

import student.connection.DB;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class pf220192_GeneralOperations implements rs.ac.bg.etf.sab.operations.GeneralOperations {

    @Override
    public void eraseAll() {
        try (var statement = DB.getConnection().createStatement()) {

            var resultSet = statement.executeQuery(
                    "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE = 'BASE TABLE'"
            );

            List<String> tables = new ArrayList<>();
            while (resultSet.next()) {
                tables.add(resultSet.getString("TABLE_NAME"));
            }
            resultSet.close();

            statement.execute("EXEC sp_MSforeachtable 'ALTER TABLE ? NOCHECK CONSTRAINT ALL'");

            for (String table : tables) {
                statement.execute("DELETE FROM " + table);
            }

            statement.execute("EXEC sp_MSforeachtable 'ALTER TABLE ? CHECK CONSTRAINT ALL'");

        } catch (SQLException e) {
            System.err.println("Error in eraseAll: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
