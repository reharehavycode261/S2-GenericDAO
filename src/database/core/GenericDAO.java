package database.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class GenericDAO<T> {
    // Supposons que nous ayons une connexion à la base de données et un nom de table
    private Connection connection;
    private String tableName;

    public GenericDAO(Connection connection, String tableName) {
        this.connection = connection;
        this.tableName = tableName;
    }

    /**
     * Met à jour les champs spécifiés d'un enregistrement dans la base de données.
     * @param id L'identifiant unique de l'enregistrement à mettre à jour.
     * @param fields Un map des noms de colonnes et de leurs valeurs respectives à mettre à jour.
     * @throws SQLException en cas d'erreur SQL
     */
    public void updateFields(Object id, Map<String, Object> fields) throws SQLException {
        if (fields == null || fields.isEmpty()) {
            throw new IllegalArgumentException("Fields to update cannot be null or empty");
        }

        StringBuilder sql = new StringBuilder("UPDATE " + tableName + " SET ");
        int index = 1;
        for (String column : fields.keySet()) {
            sql.append(column).append(" = ?");
            if (index < fields.size()) {
                sql.append(", ");
            }
            index++;
        }
        sql.append(" WHERE id = ?");

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            index = 1;
            for (Object value : fields.values()) {
                stmt.setObject(index++, value);
            }
            stmt.setObject(index, id);
            stmt.executeUpdate();
        }
    }

    // ... autres méthodes existantes ...
}