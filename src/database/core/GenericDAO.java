package database.core;

import java.sql.*;
import java.util.Map;

public class GenericDAO<T> {
    private Connection connection;
    private String tableName;

    public GenericDAO(Connection connection, String tableName) {
        this.connection = connection;
        this.tableName = tableName;
    }
    
    /**
     * Met à jour des champs spécifiques d'un enregistrement dans la base de données.
     *
     * @param id L'identifiant de l'enregistrement à mettre à jour.
     * @param fields Une map contenant les noms des champs à mettre à jour et leurs nouvelles valeurs.
     * @return true si l'enregistrement a été mis à jour avec succès, false sinon.
     * @throws SQLException en cas d'erreur SQL.
     * @throws IllegalArgumentException si la map des champs est null ou vide.
     */
    public boolean updateFields(Object id, Map<String, Object> fields) throws SQLException {
        if (fields == null || fields.isEmpty()) {
            throw new IllegalArgumentException("Fields map cannot be null or empty");
        }

        StringBuilder sql = new StringBuilder("UPDATE " + tableName + " SET ");
        for (String field : fields.keySet()) {
            sql.append(field).append(" = ?, ");
        }
        sql.setLength(sql.length() - 2); // Supprimer la dernière virgule
        sql.append(" WHERE id = ?");

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            int index = 1;
            for (Object value : fields.values()) {
                stmt.setObject(index++, value);
            }
            stmt.setObject(index, id);

            return stmt.executeUpdate() > 0;
        }
    }

    // Autres méthodes existantes du GenericDAO...

}