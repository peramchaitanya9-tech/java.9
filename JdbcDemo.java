import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcDemo {

    // Database connection parameters (Adjust these to match your database)
    // Example shown for MySQL. See comments below for PostgreSQL / SQLite / Oracle.
    private static final String URL = "jdbc:mysql://localhost:3306/testdb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    public static void main(String[] args) {
        System.out.println("Connecting to database...");

        // Using try-with-resources ensures Connection, Statement, and ResultSet are closed automatically
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.println("Connection successful!");

            // 1. Create a sample table if it does not exist
            createTable(connection);

            // 2. Insert sample record using PreparedStatement
            insertUser(connection, "Alice", "alice@example.com");

            // 3. Query and display data
            fetchUsers(connection);

        } catch (SQLException e) {
            System.err.println("Database error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void createTable(Connection connection) throws SQLException {
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS users (
                id INT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(100) NOT NULL,
                email VARCHAR(100) NOT NULL UNIQUE
            );
        """;

        try (Statement statement = connection.createStatement()) {
            statement.execute(createTableSQL);
            System.out.println("Checked/Created 'users' table.");
        }
    }

    private static void insertUser(Connection connection, String name, String email) throws SQLException {
        String insertSQL = "INSERT INTO users (name, email) VALUES (?, ?) ON DUPLICATE KEY UPDATE name = VALUES(name);";

        // PreparedStatement prevents SQL injection attacks
        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Inserted/Updated row count: " + rowsAffected);
        }
    }

    private static void fetchUsers(Connection connection) throws SQLException {
        String querySQL = "SELECT id, name, email FROM users";

        try (PreparedStatement pstmt = connection.prepareStatement(querySQL);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.println("\n--- Registered Users ---");
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                System.out.printf("ID: %d | Name: %s | Email: %s%n", id, name, email);
            }
        }
    }
}
