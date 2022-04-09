package helpers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.testng.Assert.fail;

public class DatabaseHelper {

    private static final String url = "jdbc:postgresql://localhost:5432/test";
    private static final String user = "postgres";
    private static final String password = "admin";

    protected static void loadDriver() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    protected static Connection open() {
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException ex) {
            fail("SQLException: " + ex.getMessage());
        }
        return null;
    }

    public static Connection getConnection() {
        loadDriver();
        return  open();
    }

}
