package database.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class GenericDAO<T> {
    private Connection connection;
    private String tableName;

    // Constructeur et autres méthodes existantes...

    /**
     * Met à jour certains champs d'un enregistrement dans la base de données.
     *
     * @param id L'identifiant unique de l'enregistrement à mettre à jour
     * @param fieldsToUpdate Carte des champs à mettre à jour avec leurs nouvelles valeurs
     * @throws SQLException En cas d'erreur SQL
     */
    public void updateFields(Object id, Map<String, Object> fieldsToUpdate) throws SQLException {
        if (fieldsToUpdate == null || fieldsToUpdate.isEmpty()) {
            throw new IllegalArgumentException("No fields to update");
        }

        StringBuilder sql = new StringBuilder("UPDATE ").append(tableName).append(" SET ");
        for (String field : fieldsToUpdate.keySet()) {
            sql.append(field).append(" = ?, ");
        }
        sql.setLength(sql.length() - 2); // Supprime la dernière virgule
        sql.append(" WHERE id = ?");

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            int index = 1;
            for (Object value : fieldsToUpdate.values()) {
                stmt.setObject(index++, value);
            }
            stmt.setObject(index, id);
            stmt.executeUpdate();
        }
    }
    
    // ... autres méthodes existantes ...
}