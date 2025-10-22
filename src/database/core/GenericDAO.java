package database.core;

import java.sql.*;
import java.util.*;

public class GenericDAO<T> {
    protected Connection connection;
    protected String tableName;
    protected String primaryKey;

    public GenericDAO(Connection connection, String tableName, String primaryKey) {
        this.connection = connection;
        this.tableName = tableName;
        this.primaryKey = primaryKey;
    }

    // Méthode existante pour trouver un objet par son ID
    public T findById(Object id) throws SQLException {
        String sql = "SELECT * FROM " + tableName + " WHERE " + primaryKey + "=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Conversion logique à partir de ResultSet à un objet
                }
            }
        }
        return null;
    }

    // Nouvelle méthode pour mettre à jour les champs de l'objet
    public boolean updateFields(Object id, Map<String, Object> fields) throws SQLException {
        if (fields == null || fields.isEmpty()) {
            return false;
        }
        
        StringBuilder sql = new StringBuilder("UPDATE " + tableName + " SET ");
        fields.forEach((key, value) -> sql.append(key).append("=?, "));
        sql.setLength(sql.length() - 2); // Supprimer le dernier ", "
        sql.append(" WHERE ").append(primaryKey).append("=?");

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            int index = 1;
            for (Object value : fields.values()) {
                stmt.setObject(index++, value);
            }
            stmt.setObject(index, id);
            return stmt.executeUpdate() > 0;
        }
    }

    // Autres méthodes existantes...
}