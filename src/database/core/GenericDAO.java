package database.core;

import java.sql.*;
import java.util.*;

public class GenericDAO<T> {
    protected Connection connection;
    protected String tableName;
    protected String primaryKeyField;

    // ... autres parties du code existant ...

    /**
     * Met à jour les champs spécifiés de l'entité dans la base de données.
     * @param id L'identifiant de l'enregistrement à mettre à jour.
     * @param fieldsToUpdate Une carte des champs à mettre à jour avec leurs nouvelles valeurs.
     * @throws SQLException en cas d'erreur SQL.
     */
    public void updateFields(Object id, Map<String, Object> fieldsToUpdate) throws SQLException {
        if (fieldsToUpdate == null || fieldsToUpdate.isEmpty()) {
            throw new IllegalArgumentException("fieldsToUpdate ne peut pas être null ou vide");
        }

        StringBuilder sql = new StringBuilder("UPDATE " + tableName + " SET ");
        List<Object> parameters = new ArrayList<>();

        for (String field : fieldsToUpdate.keySet()) {
            sql.append(field).append(" = ?, ");
            parameters.add(fieldsToUpdate.get(field));
        }
        sql.setLength(sql.length() - 2);  // Remove the last comma
        sql.append(" WHERE ").append(primaryKeyField).append(" = ?");
        parameters.add(id);

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < parameters.size(); i++) {
                stmt.setObject(i + 1, parameters.get(i));
            }
            stmt.executeUpdate();
        }
    }

    // ... reste du code existant ...
}