package database.core;

import database.exception.SQL.AttributeMissingException;
import database.exception.SQL.AttributeTypeNotExistingException;
import database.exception.object.NotIdentifiedInDatabaseException;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class GenericDAO<T> {
    String id;

    // Autres méthodes existantes...

    /**
     * Met à jour les champs spécifiés de l'entité dans la base de données.
     * 
     * @param dbConnection La connexion à la base de données
     * @param entity L'entité à mettre à jour
     * @param fieldsToUpdate Les champs et leurs nouvelles valeurs
     * @throws SQLException En cas d'erreur SQL
     * @throws NoSuchFieldException Si un des champs n'existe pas dans l'entité
     * @throws IllegalAccessException Si un accès illégal à un champ s'est produit
     */
    public void updateFields(DBConnection dbConnection, T entity, Map<String, Object> fieldsToUpdate)
            throws SQLException, NoSuchFieldException, IllegalAccessException, AttributeMissingException {
        StringBuilder sql = new StringBuilder("UPDATE " + entity.getClass().getSimpleName() + " SET ");
        for (String field : fieldsToUpdate.keySet()) {
            sql.append(field).append(" = ?, ");
        }
        sql.setLength(sql.length() - 2); // Remove last comma
        sql.append(" WHERE id = ?");

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql.toString())) {

            int index = 1;
            for (Map.Entry<String, Object> entry : fieldsToUpdate.entrySet()) {
                Field field = entity.getClass().getDeclaredField(entry.getKey());
                field.setAccessible(true);
                stmt.setObject(index++, entry.getValue());
            }

            Field idField = entity.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            stmt.setObject(index, idField.get(entity));

            if (stmt.executeUpdate() == 0) {
                throw new AttributeMissingException("Aucune ligne mise à jour, assurez-vous que l'ID est correct.");
            }
        }
    }

    // ... reste du code existant ...
}