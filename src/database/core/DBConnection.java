package database.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBConnection {
    private Connection connection;
    
    // Constructeur et autres méthodes existantes ...

    /**
     * Met à jour des champs spécifiques dans la table de la base de données.
     * 
     * @param tableName Le nom de la table à mettre à jour
     * @param fields Un tableau des champs à mettre à jour
     * @param values Un tableau des valeurs correspondantes
     * @param condition La condition WHERE pour spécifier les lignes à mettre à jour
     * @throws SQLException si une erreur de base de données se produit
     */
    public void updateFields(String tableName, String[] fields, Object[] values, String condition) throws SQLException {
        if (fields.length != values.length) {
            throw new IllegalArgumentException("Fields and values array must have the same length");
        }

        StringBuilder sql = new StringBuilder("UPDATE " + tableName + " SET ");
        for (int i = 0; i < fields.length; i++) {
            sql.append(fields[i]).append(" = ?");
            if (i < fields.length - 1) {
                sql.append(", ");
            }
        }
        sql.append(" WHERE ").append(condition);

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < values.length; i++) {
                ps.setObject(i + 1, values[i]);
            }
            ps.executeUpdate();
        }
    }
}