package database.core;

import java.sql.*;

/**
 * Une classe générique pour les opérations de base de données.
 * @param <T> Le type d'objet manipulé par ce DAO.
 */
public class GenericDAO<T> {
    private final Connection connection;
    private final String tableName;

    public GenericDAO(Connection connection, String tableName) {
        this.connection = connection;
        this.tableName = tableName;
    }

    /**
     * Met à jour des champs spécifiques d'un enregistrement dans la base de données.
     * @param fields Les champs à mettre à jour.
     * @param values Les valeurs correspondant aux champs à mettre à jour.
     * @param condition La condition pour déterminer quel enregistrement mettre à jour.
     * @throws SQLException En cas d'erreur SQL.
     */
    public void updateFields(String[] fields, Object[] values, String condition) throws SQLException {
        if (fields.length != values.length) {
            throw new IllegalArgumentException("Le nombre de champs doit correspondre au nombre de valeurs.");
        }
        
        StringBuilder sql = new StringBuilder("UPDATE ").append(tableName).append(" SET ");
        for (int i = 0; i < fields.length; i++) {
            sql.append(fields[i]).append(" = ?");
            if (i < fields.length - 1) {
                sql.append(", ");
            }
        }
        sql.append(" WHERE ").append(condition);

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < values.length; i++) {
                stmt.setObject(i + 1, values[i]);
            }
            stmt.executeUpdate();
        }
    }
    
    // Autres méthodes existantes du GenericDAO...
}