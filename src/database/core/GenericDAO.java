package database.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.Map;

public abstract class GenericDAO<T> {
    protected Connection connection;
    protected String tableName;

    // Méthode pour mettre à jour les champs spécifiés d'un enregistrement dans la base de données
    public boolean updateFields(int id, Map<String, Object> fieldsToUpdate) throws SQLException {
        if (fieldsToUpdate == null || fieldsToUpdate.isEmpty()) {
            throw new IllegalArgumentException("fieldsToUpdate ne peut pas être vide");
        }

        StringBuilder sql = new StringBuilder("UPDATE " + tableName + " SET ");
        fieldsToUpdate.forEach((key, value) -> sql.append(key).append(" = ?, "));
        sql.setLength(sql.length() - 2);  // Retrait de la dernière virgule
        sql.append(" WHERE id = ?");

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            int index = 1;
            for (Object value : fieldsToUpdate.values()) {
                stmt.setObject(index++, value);
            }
            stmt.setInt(index, id);
            return stmt.executeUpdate() > 0;
        }
    }

    // Méthode de pagination pour récupérer des pages de résultats
    public ResultSet getPagedResults(int pageNumber, int pageSize) throws SQLException {
        if (pageNumber < 1 || pageSize < 1) {
            throw new IllegalArgumentException("pageNumber et pageSize doivent être positifs");
        }

        String sql = "SELECT * FROM " + tableName + " LIMIT ? OFFSET ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, pageSize);
            stmt.setInt(2, (pageNumber - 1) * pageSize);
            return stmt.executeQuery();
        }
    }

    // Autres méthodes existantes...
}