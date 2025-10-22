package database.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.stream.IntStream;

public class GenericDAO<T> {
    private String tableName;

    public GenericDAO(String tableName) {
        this.tableName = tableName;
    }

    /**
     * Met à jour des champs spécifiques d'un objet en base de données
     * @param dbConnection Connexion à la base de données
     * @param fields Les noms des champs à mettre à jour
     * @param values Les valeurs correspondantes pour chaque champ
     * @param condition La condition pour identifier l'enregistrement à mettre à jour
     * @throws SQLException en cas d'erreur SQL
     */
    public void updateFields(DBConnection dbConnection, String[] fields, Object[] values, String condition) throws SQLException {
        if (fields.length != values.length) {
            throw new IllegalArgumentException("Le nombre de champs doit être égal au nombre de valeurs.");
        }

        // Construire la requête SQL UPDATE
        StringBuilder sql = new StringBuilder("UPDATE " + tableName + " SET ");
        IntStream.range(0, fields.length).forEach(i -> sql.append(fields[i]).append(" = ?, "));
        
        // Supprimer la dernière virgule et espace
        sql.setLength(sql.length() - 2);

        sql.append(" WHERE ").append(condition);

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql.toString())) {

            // Assigner les valeurs aux placeholders
            for (int i = 0; i < values.length; i++) {
                preparedStatement.setObject(i + 1, values[i]);
            }

            // Exécution de la mise à jour
            preparedStatement.executeUpdate();
        }
    }
}