package database.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.StringJoiner;

public class GenericDAO<T> {
    // Existant code...

    /**
     * Met à jour des champs spécifiques d'une table de la base de données.
     * 
     * @param dbConnection La connexion à la base de données.
     * @param fields Les champs à mettre à jour.
     * @param values Les nouvelles valeurs des champs.
     * @param condition La condition SQL pour déterminer les lignes à mettre à jour.
     * @throws SQLException en cas d'erreur SQL lors de la mise à jour.
     */
    public void updateFields(DBConnection dbConnection, String[] fields, Object[] values, String condition) throws SQLException {
        if (fields.length != values.length) {
            throw new IllegalArgumentException("Le nombre de champs et de valeurs doit être le même");
        }

        StringJoiner setClause = new StringJoiner(", ");
        for (String field : fields) {
            setClause.add(field + " = ?");
        }

        String sql = "UPDATE " + getTableName() + " SET " + setClause.toString() + " WHERE " + condition;

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            for (int i = 0; i < values.length; i++) {
                preparedStatement.setObject(i + 1, values[i]);
            }
            
            preparedStatement.executeUpdate();
        }
    }

    // Exemple d'une méthode pour obtenir le nom de la table, peut être personnalisé selon vos besoins
    private String getTableName() {
        // Supposons que la classe T ait une méthode spécifique pour cela
        return "table_name"; // Doit être remplacé par le nom réel de la table
    }
    
    // ... Code existant
}