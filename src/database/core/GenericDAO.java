package database.core;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GenericDAO<T> {
    private final Connection connection;

    public GenericDAO(Connection connection) {
        this.connection = connection;
    }
    
    public List<T> findAllPaged(int limit, int offset) throws SQLException {
        String tableName = getTableName();
        String sql = "SELECT * FROM " + tableName + " LIMIT ? OFFSET ?";
        List<T> results = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            stmt.setInt(2, offset);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                T entity = mapResultSetToEntity(rs);
                results.add(entity);
            }
        }
        return results;
    }
    
    private String getTableName() {
        // Logic to determine table name
        return "my_table";
    }
    
    private T mapResultSetToEntity(ResultSet rs) throws SQLException {
        // Logic to map ResultSet to entity
        return null;
    }
}