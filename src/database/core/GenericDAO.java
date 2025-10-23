package database.core;

import java.sql.*;
import java.util.*;

public class GenericDAO<T> {
    private Connection connection;
    private String tableName;

    public GenericDAO(Connection connection, String tableName) {
        this.connection = connection;
        this.tableName = tableName;
    }
    
    // Méthode pour mettre à jour les champs spécifiés
    public void updateFields(Map<String, Object> fields, String whereClause) throws SQLException {
        StringBuilder sql = new StringBuilder("UPDATE " + tableName + " SET ");
        
        int i = 0;
        for (String column : fields.keySet()) {
            sql.append(column).append(" = ?");
            if (i < fields.size() - 1) {
                sql.append(", ");
            }
            i++;
        }
        
        sql.append(" WHERE ").append(whereClause);
        
        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            int index = 1;
            for (Object value : fields.values()) {
                stmt.setObject(index, value);
                index++;
            }
            stmt.executeUpdate();
        }
    }
    
    // Méthode pour récupérer les résultats avec pagination
    public List<T> getPaginatedResults(int pageSize, int pageNumber) throws SQLException {
        String sql = "SELECT * FROM " + tableName + " LIMIT ? OFFSET ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            int offset = (pageNumber - 1) * pageSize;
            stmt.setInt(1, pageSize);
            stmt.setInt(2, offset);
            
            try (ResultSet rs = stmt.executeQuery()) {
                List<T> results = new ArrayList<>();
                while (rs.next()) {
                    // Assuming there's a method to parse a row into instance of T
                    T item = parseRow(rs);
                    results.add(item);
                }
                return results;
            }
        }
    }
    
    // Exemple de méthode pour convertir un ResultSet en objet T
    private T parseRow(ResultSet rs) throws SQLException {
        // Logique de conversion du ResultSet en instance de T
        return null; // Placeholder
    }
}