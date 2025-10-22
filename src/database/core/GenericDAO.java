package database.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GenericDAO<T> {
    // ... autres méthodes existantes ...

    /**
     * Met à jour certains champs spécifiques d'un objet dans la base de données.
     * 
     * @param dbConnection Connexion à la base de données
     * @param fields Champs à mettre à jour
     * @param values Valeurs à appliquer aux champs
     * @param condition Condition SQL pour cibler les enregistrements à mettre à jour
     * @throws SQLException En cas d'erreur SQL
     */
    public void updateFields(DBConnection dbConnection, String[] fields, Object[] values, String condition) throws SQLException {
        if (fields.length != values.length) {
            throw new IllegalArgumentException("Le nombre de champs et de valeurs doit être le même.");
        }

        StringBuilder sql = new StringBuilder("UPDATE " + getTableName() + " SET ");
        for (int i = 0; i < fields.length; i++) {
            sql.append(fields[i]).append(" = ?");
            if (i < fields.length - 1) {
                sql.append(", ");
            }
        }
        sql.append(" WHERE ").append(condition);

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < values.length; i++) {
                stmt.setObject(i + 1, values[i]);
            }
            stmt.executeUpdate();
        }
    }

    // méthode fictive pour obtenir le nom de la table, à adapter selon votre implémentation réelle
    private String getTableName() {
        return "your_table_name";
    }

    // ... autres méthodes existantes ...
}