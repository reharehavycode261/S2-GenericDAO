package database.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GenericDAO<T> {
    private Connection connection;

    public GenericDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Met à jour les champs spécifiés d'un objet dans la base de données.
     * 
     * @param dbConnection La connexion active à la base de données.
     * @param table Le nom de la table à mettre à jour.
     * @param fields Les champs à mettre à jour.
     * @param values Les nouvelles valeurs des champs.
     * @param condition La condition SQL pour spécifier quels enregistrements mettre à jour.
     * @throws SQLException en cas d'erreur SQL.
     */
    public void updateFields(DBConnection dbConnection, String table, String[] fields, Object[] values, String condition) throws SQLException {
        if (fields.length != values.length) {
            throw new IllegalArgumentException("Le nombre de champs doit correspondre au nombre de valeurs.");
        }
        
        StringBuilder sql = new StringBuilder("UPDATE " + table + " SET ");
        
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
    
    // Ceci est un exemple des autres méthodes qui pourraient exister dans la classe
    // ... autres méthodes ...
}