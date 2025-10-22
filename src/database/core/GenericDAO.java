package database.core;

import database.exception.SQL.AttributeMissingException;
import database.exception.SQL.AttributeTypeNotExistingException;
import database.exception.object.NotIdentifiedInDatabaseException;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class GenericDAO {
    String id;

    // ... autres méthodes ...

    /**
     * Met à jour certains champs d'un enregistrement dans la base de données
     * @param dbConnection La connexion à la base de données
     * @param tableName Le nom de la table à mettre à jour
     * @param id L'identifiant de l'enregistrement à mettre à jour
     * @param fieldsToUpdate Une map qui contient le nom des champs et leurs nouvelles valeurs
     * @throws SQLException en cas d'erreur SQL
     */
    public void updateFields(DBConnection dbConnection, String tableName, String id, Map<String, Object> fieldsToUpdate) throws SQLException {
        if (fieldsToUpdate == null || fieldsToUpdate.isEmpty()) {
            throw new IllegalArgumentException("fieldsToUpdate ne peut pas être vide");
        }

        StringBuilder sql = new StringBuilder("UPDATE " + tableName + " SET ");
        for (String fieldName : fieldsToUpdate.keySet()) {
            sql.append(fieldName).append(" = ?, ");
        }
        sql.setLength(sql.length() - 2);  // Retirer la dernière virgule
        sql.append(" WHERE id = ?");

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql.toString())) {

            int index = 1;
            for (Object value : fieldsToUpdate.values()) {
                stmt.setObject(index++, value);
            }
            stmt.setObject(index, id);

            stmt.executeUpdate();
        }
    }

    // ... autres méthodes ...
}