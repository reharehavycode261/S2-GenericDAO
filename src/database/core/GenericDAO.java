package database.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GenericDAO<T> {
    // ... code existant ...
    
    /**
     * Met à jour les champs spécifiques d'un enregistrement dans la base de données
     * @param dbConnection La connexion à la base de données
     * @param fields Les champs à mettre à jour
     * @param values Les nouvelles valeurs à affecter aux champs
     * @param condition La condition SQL (par ex., "id = 1")
     * @throws SQLException en cas d'erreur SQL
     */
    public void updateFields(DBConnection dbConnection, String[] fields, Object[] values, String condition) throws SQLException {
        if (fields.length != values.length) {
            throw new IllegalArgumentException("Le nombre de champs doit correspondre au nombre de valeurs.");
        }

        StringBuilder sql = new StringBuilder("UPDATE ");
        sql.append(getTableName()); // Assure-toi d'avoir une méthode getTableName() dans ta classe
        sql.append(" SET ");

        for (int i = 0; i < fields.length; i++) {
            sql.append(fields[i]).append(" = ?");
            if (i < fields.length - 1) {
                sql.append(", ");
            }
        }

        sql.append(" WHERE ").append(condition);

        try (PreparedStatement stmt = dbConnection.getConnection().prepareStatement(sql.toString())) {
            for (int i = 0; i < values.length; i++) {
                stmt.setObject(i + 1, values[i]);
            }
            stmt.executeUpdate();
        }
    }
    
    // ... reste du code existant ...
}