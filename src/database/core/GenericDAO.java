package database.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public abstract class GenericDAO<T> {
    protected Connection connection;
    protected String tableName;

    // ... autres méthodes existantes ...

    /**
     * Met à jour les champs spécifiés d'un enregistrement dans la base de données
     * @param id Identifiant de l'enregistrement à mettre à jour
     * @param fieldsToUpdate Champs à mettre à jour avec leurs nouvelles valeurs
     * @throws SQLException en cas d'erreur SQL
     */
    public void updateFields(Object id, Map<String, Object> fieldsToUpdate) throws SQLException {
        if (fieldsToUpdate == null || fieldsToUpdate.isEmpty()) {
            throw new IllegalArgumentException("fieldsToUpdate ne doit pas être nul ou vide");
        }

        StringBuilder sql = new StringBuilder("UPDATE ").append(tableName).append(" SET ");
        for (String fieldName : fieldsToUpdate.keySet()) {
            sql.append(fieldName).append(" = ?, ");
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