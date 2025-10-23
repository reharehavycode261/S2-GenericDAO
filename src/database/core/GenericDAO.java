package database.core;

import java.sql.*;
import java.util.*;

public class GenericDAO<T> {
    protected Connection connection;
    protected String tableName;

    // ... code existant ...

    /**
     * Met à jour des champs spécifiques d'un enregistrement identifié par une clé primaire
     * @param id L'identifiant de l'enregistrement à mettre à jour
     * @param fields Les champs à mettre à jour avec leurs nouvelles valeurs
     * @throws SQLException en cas d'erreur SQL
     */
    public void updateFields(Object id, Map<String, Object> fields) throws SQLException {
        // Valider l'entrée
        if (fields == null || fields.isEmpty()) {
            throw new IllegalArgumentException("Fields to update cannot be null or empty.");
        }

        // Construire la requête SQL
        StringBuilder sql = new StringBuilder("UPDATE ").append(tableName).append(" SET ");
        for (String field : fields.keySet()) {
            sql.append(field).append(" = ?, ");
        }
        sql.delete(sql.length() - 2, sql.length()); // Supprimer la dernière virgule
        sql.append(" WHERE id = ?");

        // Préparer la requête
        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            int index = 1;
            for (Object value : fields.values()) {
                stmt.setObject(index++, value);
            }
            stmt.setObject(index, id);

            // Exécuter la mise à jour
            stmt.executeUpdate();
        }
    }

    // ... reste du code existant ...
}