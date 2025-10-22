package database.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBConnection {
    private Connection connection;

    public DBConnection(Connection connection) {
        this.connection = connection;
    }

    /**
     * Met à jour des champs spécifiques d'une table dans la base de données.
     * 
     * @param tableName Le nom de la table à mettre à jour
     * @param fields Les champs à mettre à jour
     * @param values Les valeurs correspondantes à mettre
     * @param condition La condition à respecter pour la mise à jour
     * @throws SQLException en cas d'erreur SQL
     */
    public void updateFields(String tableName, String[] fields, Object[] values, String condition) throws SQLException {
        if (fields.length != values.length) {
            throw new IllegalArgumentException("Fields and values must have the same length");
        }

        StringBuilder query = new StringBuilder("UPDATE ");
        query.append(tableName).append(" SET ");
        
        for (int i = 0; i < fields.length; i++) {
            query.append(fields[i]).append(" = ?");
            if (i < fields.length - 1) {
                query.append(", ");
            }
        }

        query.append(" WHERE ").append(condition);

        try (PreparedStatement stmt = connection.prepareStatement(query.toString())) {
            for (int i = 0; i < values.length; i++) {
                stmt.setObject(i + 1, values[i]);
            }
            stmt.executeUpdate();
        }
    }
}