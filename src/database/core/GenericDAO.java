package database.core;

import java.sql.*;
import java.util.*;

public class GenericDAO<T> {

    protected Connection connection;
    protected String tableName;

    public GenericDAO(Connection connection, String tableName) {
        this.connection = connection;
        this.tableName = tableName;
    }

    // Mise à jour des champs sélectionnés pour un enregistrement donné
    public boolean updateFields(int id, Map<String, Object> fields) throws SQLException {
        if (fields == null || fields.isEmpty()) {
            throw new IllegalArgumentException("Fields to update cannot be null or empty.");
        }

        StringBuilder sql = new StringBuilder("UPDATE ");
        sql.append(tableName).append(" SET ");

        int count = 0;
        for (String field : fields.keySet()) {
            if (count++ > 0) {
                sql.append(", ");
            }
            sql.append(field).append(" = ?");
        }
        sql.append(" WHERE id = ?");

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            int index = 1;

            for (Object value : fields.values()) {
                stmt.setObject(index++, value);
            }
            stmt.setInt(index, id);

            return stmt.executeUpdate() > 0;
        }
    }

    // Récupération avec pagination
    public List<T> findWithPagination(int limit, int offset) throws SQLException {
        List<T> results = new ArrayList<>();
        String sql = "SELECT * FROM " + tableName + " LIMIT ? OFFSET ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            stmt.setInt(2, offset);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Supposing there is a method to convert ResultSet to the desired object
                    T obj = mapResultSetToEntity(rs);
                    results.add(obj);
                }
            }
        }

        return results;
    }

    // This method would need to be implemented to map a ResultSet to the entity T
    protected T mapResultSetToEntity(ResultSet rs) throws SQLException {
        // Implementation specific to T
        return null; // Placeholder implementation
    }
}