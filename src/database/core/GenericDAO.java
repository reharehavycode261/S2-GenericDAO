package database.core;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class GenericDAO {

    // Assuming there is a method to get the database connection
    protected Connection getConnection() {
        // Implementation for obtaining the connection
        return null;
    }

    // New method count() to count records
    public int count(String tableName) {
        int count = 0;
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            String query = "SELECT COUNT(*) FROM " + tableName;
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }
}