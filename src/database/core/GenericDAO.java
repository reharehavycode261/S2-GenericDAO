package database.core;

import database.exception.SQL.AttributeMissingException;
import database.exception.SQL.AttributeTypeNotExistingException;
import database.exception.object.NotIdentifiedInDatabaseException;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.List;

public class GenericDAO {
    String id;
    Connection connection;  // Assuming we have a connection instance

    // ... code existant ...

    /**
     * Met à jour certains champs d'un enregistrement dans la base de données.
     * @param tableName Le nom de la table
     * @param id L'identifiant de l'enregistrement à mettre à jour
     * @param updates La liste des affectations de colonnes à mettre à jour
     * @throws SQLException en cas d'erreur SQL
     */
    public void updateFields(String tableName, String id, List<Affectation> updates) throws SQLException {
        if (updates == null || updates.isEmpty()) {
            throw new IllegalArgumentException("No fields to update.");
        }

        StringBuilder sql = new StringBuilder("UPDATE " + tableName + " SET ");
        for (int i = 0; i < updates.size(); i++) {
            Affectation affectation = updates.get(i);
            sql.append(affectation.getColumn()).append(" = ?");
            if (i < updates.size() - 1) {
                sql.append(", ");
            }
        }
        sql.append(" WHERE id = ?");

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            int index = 1;
            for (Affectation affectation : updates) {
                stmt.setObject(index++, affectation.getValue());
            }
            stmt.setString(index, id);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Failed to update fields: " + e.getMessage(), e);
        }
    }

    // ... reste du code existant ...
}