import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static void main(String[] args) {
        // Step 1: Define database credentials & connection URL
        String url = "jdbc:mysql://localhost:3306/your_database_name";
        String username = "root";
        String password = "your_password";

        System.out.println("Connecting to database...");

        // Step 2 & 3: Register driver and establish connection
        try {
            // (Optional in modern JDBC 4.0+, but required for older setups)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Open connection
            Connection connection = DriverManager.getConnection(url, username, password);

            if (connection != null && !connection.isClosed()) {
                System.out.println("Successfully connected to the database!");
            }

            // Step 4: Close the connection
            connection.close();
            System.out.println("Connection closed successfully.");

        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found. Please add the MySQL Connector JAR to your classpath.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database!");
            e.printStackTrace();
        }
    }
}
