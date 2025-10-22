package database.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class GenericDAO<T> {
    protected Connection connection;
    protected String tableName;

    public GenericDAO(Connection connection, String tableName) {
        this.connection = connection;
        this.tableName = tableName;
    }

    /**
     * Met à jour des champs spécifiques d'un enregistrement dans la base de données.
     * 
     * @param fieldsToUpdate Une Map contenant les champs et leurs nouvelles valeurs
     * @param whereClause La condition WHERE pour identifier l'enregistrement à mettre à jour
     * @throws SQLException en cas d'erreur SQL
     */
    public void updateFields(Map<String, Object> fieldsToUpdate, String whereClause) throws SQLException {
        if (fieldsToUpdate == null || fieldsToUpdate.isEmpty()) {
            throw new IllegalArgumentException("Fields to update cannot be null or empty");
        }
        if (whereClause == null || whereClause.trim().isEmpty()) {
            throw new IllegalArgumentException("Where clause cannot be null or empty");
        }

        StringBuilder sql = new StringBuilder("UPDATE " + tableName + " SET ");
        boolean first = true;

        for (String field : fieldsToUpdate.keySet()) {
            if (!first) {
                sql.append(", ");
            } else {
                first = false;
            }
            sql.append(field).append(" = ?");
        }

        sql.append(" WHERE ").append(whereClause);

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            int index = 1;
            for (Object value : fieldsToUpdate.values()) {
                stmt.setObject(index++, value);
            }
            stmt.executeUpdate();
        }
    }
}