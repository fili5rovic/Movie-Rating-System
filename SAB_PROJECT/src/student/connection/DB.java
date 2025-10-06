package student.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {

    private static final String username = "sa";
    private static final String password = "1234";

    private static final String serverName = "localhost";
    private static final String instanceName = ".";
    private static final int port = 1433;
    private static final String dbName = "SAB_PROJECT";

    private static final String connectionString =
            "jdbc:sqlserver://" + serverName + "\\" + instanceName + ":" + port +
                    ";databaseName=" + dbName + ";encrypt=true;trustServerCertificate=true";

    private Connection connection = null;

    private static volatile DB db;

    private DB() {
        try {
            connection = DriverManager.getConnection(connectionString, username, password);
            System.out.println("Connected to database.");
        } catch (SQLException e) {
            System.err.println("Couldn't connect to database");
            System.err.println("Error code: " + e.getErrorCode());
            System.err.println("Message: " + e.getMessage());
        }
    }

    private static DB getInstance() {
        if (db == null)
            synchronized (DB.class) {
                if(db == null) {
                    db = new DB();
                }
            }
        return db;
    }

    public static Connection getConnection() {
        return getInstance().connection;
    }

    public static void closeConnection() {
        if(db == null)
            return;
        Connection connection = getInstance().connection;
        try {
            if(connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connection closed.");
                db = null;
            }
        } catch (SQLException e) {
            System.err.println("Couldn't close connection.");
            System.err.println(e.getMessage());
        }
    }

}
