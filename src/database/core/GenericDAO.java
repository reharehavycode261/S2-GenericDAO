package database.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.StringJoiner;

public class GenericDAO<T> {
    private Connection connection;

    public GenericDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Met à jour les champs spécifiés d'un enregistrement dans la base de données.
     * @param id L'identifiant de l'enregistrement à mettre à jour
     * @param fieldsToUpdate Map des champs et de leurs nouvelles valeurs
     * @throws SQLException en cas d'erreur SQL
     */
    public void updateFields(String id, Map<String, Object> fieldsToUpdate) throws SQLException {
        if (fieldsToUpdate == null || fieldsToUpdate.isEmpty()) {
            throw new IllegalArgumentException("fieldsToUpdate ne doit pas être vide.");
        }

        String tableName = this.getClass().getSimpleName().toLowerCase();
        StringJoiner setClause = new StringJoiner(", ");

        for (String field : fieldsToUpdate.keySet()) {
            setClause.add(field + " = ?");
        }

        String sql = "UPDATE " + tableName + " SET " + setClause.toString() + " WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            int index = 1;
            for (Object value : fieldsToUpdate.values()) {
                stmt.setObject(index++, value);
            }
            stmt.setString(index, id);
            stmt.executeUpdate();
        }
    }

    // ... autres méthodes existantes ...
}